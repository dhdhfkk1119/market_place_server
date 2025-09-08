package com.market.market_place.members.services;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.market.market_place._core._exception.Exception400;
import com.market.market_place._core._exception.Exception401;
import com.market.market_place._core._exception.Exception404;
import com.market.market_place._core._utils.JwtUtil;
import com.market.market_place.email.VerificationPurpose;
import com.market.market_place.email.dtos.*;
import com.market.market_place.email.services.EmailVerificationService;
import com.market.market_place.members.domain.Member;
import com.market.market_place.members.domain.MemberStatus;
import com.market.market_place.members.domain.RefreshToken;
import com.market.market_place.members.dto_auth.*;
import com.market.market_place.members.dto_token.LoginResponseWithTokens;
import com.market.market_place.members.dto_token.TokenReissueResponse;
import com.market.market_place.members.repositories.MemberRepository;
import com.market.market_place.members.repositories.RefreshTokenRepository;
import com.market.market_place.terms.dtos.AgreeTermsRequestDto;
import com.market.market_place.terms.services.TermsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberAuthService {

    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final TermsService termsService;
    private final MemberService memberService;
    private final EmailVerificationService emailVerificationService;

    // 회원가입
    @Transactional
    public MemberRegisterResponse registerMember(MemberRegisterRequest request) {
        log.info("회원가입 시작. 로그인 ID: {}, 이메일: {}", request.getLoginId(), request.getEmail());
        if (request.getIsEmailVerified() == null || !request.getIsEmailVerified()) {
            throw new Exception400("이메일 인증이 완료되지 않았습니다.");
        }
        if (memberRepository.findByLoginId(request.getLoginId())
                            .isPresent()) {
            throw new Exception400("이미 사용 중인 아이디입니다.");
        }
        if (memberRepository.findByEmail(request.getEmail())
                                .isPresent()) {
            throw new Exception400("이미 사용 중인 이메일입니다.");
        }
        Member newMember = Member.from(request, passwordEncoder);
        Member savedMember = memberRepository.save(newMember);
        AgreeTermsRequestDto agreeTermsRequest = new AgreeTermsRequestDto();
        agreeTermsRequest.setAgreedTermIds(request.getAgreedTermIds());
        termsService.agreeTerms(agreeTermsRequest, savedMember);
        log.info("회원가입 성공. 사용자 ID: {}", savedMember.getId());
        return new MemberRegisterResponse(savedMember);
    }

    // 회원가입용 인증 코드 발송 요청 처리
    public void sendRegistrationCode(String email) {
        if (memberRepository.findByEmail(email)
                                .isPresent()) {
            throw new Exception400("이미 가입된 이메일입니다.");
        }
        emailVerificationService.sendCode(email, VerificationPurpose.REGISTER);
    }

    // 로그인
    @Transactional // 리프레시 토큰 저장/업데이트를 위해 트랜잭션 추가
    public LoginResponseWithTokens login(MemberLoginRequest request) { // 반환 타입 변경
        log.info("로그인 시도. 로그인 ID: {}", request.getLoginId());
        Member member = memberRepository.findByLoginId(request.getLoginId())
                                        .orElseThrow(() -> new Exception401("아이디 또는 비밀번호가 일치하지 않습니다."));

        // [수정] 계정 상태 확인 로직 강화
        if (member.getStatus() == MemberStatus.BANNED) {
            log.warn("정지된 계정 로그인 시도. 사용자 ID: {}", member.getId());
            throw new Exception401("활동이 정지된 계정입니다.");
        }
        if (member.getStatus() == MemberStatus.WITHDRAWN) {
            log.warn("탈퇴한 계정 로그인 시도. 사용자 ID: {}", member.getId());
            throw new Exception401("이미 탈퇴한 계정입니다.");
        }
        // ACTIVE가 아닌 다른 상태가 추가될 경우를 대비한 방어 코드
        if (member.getStatus() != MemberStatus.ACTIVE) {
            log.error("처리되지 않은 계정 상태 로그인 시도. 사용자 ID: {}, 상태: {}", member.getId(), member.getStatus());
            throw new Exception401("로그인할 수 없는 계정 상태입니다.");
        }

        if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            log.warn("비밀번호 불일치. 로그인 ID: {}", request.getLoginId());
            throw new Exception401("아이디 또는 비밀번호가 일치하지 않습니다.");
        }

        // 1. 액세스 토큰, 리프레시 토큰 생성
        String accessToken = JwtUtil.createAccessToken(member);
        String refreshToken = JwtUtil.createRefreshToken(member);

        // 2. 리프레시 토큰을 DB에 저장 (해싱하여 저장)
        String hashedRefreshToken = hashToken(refreshToken);

        refreshTokenRepository.findByMember(member)
            .ifPresentOrElse(
                rt -> rt.updateTokenValue(hashedRefreshToken), // 기존 토큰이 있으면 값 업데이트
                () -> refreshTokenRepository.save(RefreshToken.builder().member(member).tokenValue(hashedRefreshToken).build()) // 없으면 새로 생성
            );

        MemberLoginResponse responseDTO = new MemberLoginResponse(member);
        log.info("로그인 성공. 사용자 ID: {}", member.getId());

        // LoginResponseWithTokens에 액세스 토큰, 리프레시 토큰, 만료 시간을 모두 담아 반환
        return new LoginResponseWithTokens(accessToken, refreshToken, JwtUtil.REFRESH_TOKEN_EXPIRATION_TIME / 1000, responseDTO);
    }

    // 토큰 재발급
    @Transactional
    public TokenReissueResponse reissueTokens(String refreshToken) {
        log.info("토큰 재발급 요청. 리프레시 토큰: {}", refreshToken);
        Long memberId;
        try {
            // 1. 리프레시 토큰 유효성 검증 및 memberId 추출
            memberId = JwtUtil.getMemberIdFromRefreshToken(refreshToken);
        } catch (JWTVerificationException e) {
            log.warn("유효하지 않거나 만료된 리프레시 토큰: {}", e.getMessage());
            throw new Exception401("유효하지 않거나 만료된 리프레시 토큰입니다.");
        }

        // 2. DB에서 해당 memberId의 리프레시 토큰 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new Exception404("해당 회원을 찾을 수 없습니다."));

        RefreshToken storedRefreshToken = refreshTokenRepository.findByMember(member)
                .orElseThrow(() -> new Exception401("인증되지 않은 사용자입니다. 리프레시 토큰이 존재하지 않습니다."));

        // 3. 클라이언트가 보낸 토큰의 해시값과 DB에 저장된 해시값 비교
        if (!hashToken(refreshToken).equals(storedRefreshToken.getTokenValue())) {
            log.warn("리프레시 토큰 불일치. 사용자 ID: {}", memberId);
            throw new Exception401("토큰 정보가 일치하지 않습니다.");
        }

        // 4. 새로운 액세스 토큰 및 리프레시 토큰 생성 (Refresh Token Rotation)
        String newAccessToken = JwtUtil.createAccessToken(member);
        String newRefreshToken = JwtUtil.createRefreshToken(member);

        // 5. DB에 저장된 리프레시 토큰 값 업데이트
        storedRefreshToken.updateTokenValue(hashToken(newRefreshToken));

        log.info("토큰 재발급 성공. 사용자 ID: {}", memberId);
        return new TokenReissueResponse(newAccessToken, newRefreshToken, JwtUtil.REFRESH_TOKEN_EXPIRATION_TIME / 1000); // maxAge 추가
    }

    // 비밀번호 변경
    @Transactional
    public void changePassword(Long memberId, ChangePasswordRequest request) {
        log.info("비밀번호 변경 시작. 사용자 ID: {}", memberId);
        Member member = memberService.findMember(memberId);
        if (!passwordEncoder.matches(request.getCurrentPassword(), member.getPassword())) {
            throw new Exception401("현재 비밀번호가 일치하지 않습니다.");
        }
        String newEncodedPassword = passwordEncoder.encode(request.getNewPassword());
        member.updatePassword(newEncodedPassword);
        log.info("비밀번호 변경 완료. 사용자 ID: {}", memberId);
    }

    //비밀번호 재설정을 위한 인증 코드를 이메일로 발송합니다.
    public void sendPasswordResetCode(SendPasswordResetCodeRequest request) {
        log.info("비밀번호 재설정 코드 발송 요청. 로그인 ID: {}", request.getLoginId());
        Member member = memberRepository.findByLoginId(request.getLoginId())
                                        .orElseThrow(() -> new Exception404("해당 아이디를 가진 회원을 찾을 수 없습니다."));

        if (!Objects.equals(member.getEmail(), request.getEmail())) {
            throw new Exception400("아이디와 이메일 정보가 일치하지 않습니다.");
        }

        emailVerificationService.sendCode(request.getEmail(), VerificationPurpose.RESET_PASSWORD);
    }

    //이메일로 받은 인증 코드를 검증하고, 성공 시 비밀번호 재설정용 임시 토큰을 발급합니다.
    public PasswordResetTokenResponse confirmPasswordResetCode(ConfirmVerificationRequest request) {
        log.info("비밀번호 재설정 코드 검증 요청. 이메일: {}", request.getEmail());
        boolean isVerified = emailVerificationService.verifyCode(request.getEmail(), VerificationPurpose.RESET_PASSWORD, request.getCode());

        if (!isVerified) {
            throw new Exception400("인증 코드가 유효하지 않거나 만료되었습니다.");
        }

        Member member = memberRepository.findByEmail(request.getEmail())
                                            .orElseThrow(() -> new Exception404("해당 이메일을 가진 회원을 찾을 수 없습니다."));

        String resetToken = JwtUtil.createPasswordResetToken(member);
        log.info("비밀번호 재설정 임시 토큰 발급 완료. 사용자 ID: {}", member.getId());

        return new PasswordResetTokenResponse(resetToken);
    }

    //발급받은 임시 토큰을 사용하여 최종적으로 비밀번호를 재설정합니다.
    @Transactional
    public void resetPassword(PasswordResetRequest request) {
        log.info("최종 비밀번호 재설정 요청.");
        JwtUtil.SessionUser sessionUser = JwtUtil.verifyPasswordResetToken(request.getResetToken());

        Member member = memberService.findMember(sessionUser.getId());
        String newEncodedPassword = passwordEncoder.encode(request.getNewPassword());
        member.updatePassword(newEncodedPassword);
        log.info("최종 비밀번호 재설정 완료. 사용자 ID: {}", member.getId());
    }

    //아이디 찾기를 위한 인증 코드를 이메일로 발송
    public void sendFindIdCode(String email) {
        log.info("아이디 찾기 코드 발송 요청. 이메일: {}", email);
        if (memberRepository.findByEmail(email)
                                .isEmpty()) {
            throw new Exception404("해당 이메일로 가입된 회원을 찾을 수 없습니다.");
        }
        emailVerificationService.sendCode(email, VerificationPurpose.FIND_ID);
    }

    //이메일로 받은 인증 코드를 검증하고, 성공 시 마스킹 처리된 아이디를 반환
    public FindIdResponse findLoginIdByEmail(ConfirmVerificationRequest request) {
        log.info("아이디 찾기 코드 검증 요청. 이메일: {}", request.getEmail());
        boolean isVerified = emailVerificationService.verifyCode(request.getEmail(), VerificationPurpose.FIND_ID, request.getCode());
        if (!isVerified) {
            throw new Exception400("인증 코드가 유효하지 않거나 만료되었습니다.");
        }
        Member member = memberRepository.findByEmail(request.getEmail())
                                            .orElseThrow(() -> new Exception404("해당 이메일을 가진 회원을 찾을 수 없습니다."));
        String maskedLoginId = maskLoginId(member.getLoginId());
        log.info("아이디 찾기 성공. 사용자 ID: {}", member.getId());
        return new FindIdResponse(maskedLoginId);
    }

    private String maskLoginId(String loginId) {
        if (loginId == null || loginId.length() <= 3) {
            return loginId;
        }
        return loginId.substring(0, 3) + "***";
    }

    // SHA-256 해싱 함수
    private String hashToken(String token) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(token.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            log.error("SHA-256 algorithm not found", e);
            throw new RuntimeException("Failed to hash token", e);
        }
    }
}
