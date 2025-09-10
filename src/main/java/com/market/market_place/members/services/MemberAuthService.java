package com.market.market_place.members.services;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.market.market_place._core._exception.Exception400;
import com.market.market_place._core._exception.Exception401;
import com.market.market_place._core._exception.Exception404;
import com.market.market_place._core._utils.JwtUtil;
import com.market.market_place.members.domain.*;
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

    // 일반 회원가입 처리
    @Transactional
    public MemberRegisterResponse registerMember(MemberRegisterRequest request) {
        log.info("회원가입 시작. 로그인 ID: {}, 이메일: {}", request.getLoginId(), request.getEmail());
        // 이메일 인증 여부 확인
        if (request.getIsEmailVerified() == null || !request.getIsEmailVerified()) {
            throw new Exception400("이메일 인증이 완료되지 않았습니다.");
        }
        // 아이디 및 이메일 중복 확인
        if (memberRepository.existsByLoginId(request.getLoginId())) {
            throw new Exception400("이미 사용 중인 아이디입니다.");
        }
        if (memberRepository.existsByEmail(request.getEmail())) {
            throw new Exception400("이미 사용 중인 이메일입니다.");
        }
        // 회원 생성 및 약관 동의
        Member newMember = Member.from(request, passwordEncoder);
        Member savedMember = memberRepository.save(newMember);
        AgreeTermsRequestDto agreeTermsRequest = new AgreeTermsRequestDto();
        agreeTermsRequest.setAgreedTermIds(request.getAgreedTermIds());
        termsService.agreeTerms(agreeTermsRequest, savedMember);
        log.info("회원가입 성공. 사용자 ID: {}", savedMember.getId());
        return new MemberRegisterResponse(savedMember);
    }

    // 일반 로그인 처리
    @Transactional
    public LoginResponseWithTokens login(MemberLoginRequest request) {
        log.info("로그인 시도. 로그인 ID: {}", request.getLoginId());
        // 아이디로 회원 조회
        Member member = memberRepository.findByLoginId(request.getLoginId())
                .orElseThrow(() -> new Exception401("아이디 또는 비밀번호가 일치하지 않습니다."));

        // 비밀번호 검증
        if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            log.warn("비밀번호 불일치. 로그인 ID: {}", request.getLoginId());
            throw new Exception401("아이디 또는 비밀번호가 일치하지 않습니다.");
        }

        // 계정 상태 확인 및 토큰 발급
        checkAccountStatus(member);
        log.info("로그인 성공. 사용자 ID: {}", member.getId());
        return issueTokensAndGetResponse(member);
    }

    // 소셜 로그인 처리
    @Transactional
    public LoginResponseWithTokens socialLogin(SocialLoginRequest request) {
        log.info("소셜 로그인 시도. Provider: {}, Email: {}", request.getProvider(), request.getEmail());

        // 소셜 정보로 회원 조회 또는 신규 생성
        Member member = memberRepository.findByProviderAndProviderId(request.getProvider(), request.getProviderId())
                .orElseGet(() -> createSocialMember(request));

        // 계정 상태 확인 및 토큰 발급
        checkAccountStatus(member);
        log.info("소셜 로그인 성공. 사용자 ID: {}", member.getId());
        return issueTokensAndGetResponse(member);
    }

    // JWT 토큰 재발급
    @Transactional
    public TokenReissueResponse reissueTokens(String refreshToken) {
        log.info("토큰 재발급 요청.");
        // Refresh Token 검증 및 memberId 추출
        Long memberId;
        try {
            memberId = JwtUtil.getMemberIdFromRefreshToken(refreshToken);
        } catch (JWTVerificationException e) {
            log.warn("유효하지 않거나 만료된 리프레시 토큰: {}", e.getMessage());
            throw new Exception401("유효하지 않거나 만료된 리프레시 토큰입니다.");
        }

        // DB에서 Refresh Token 조회 및 비교
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new Exception404("해당 회원을 찾을 수 없습니다."));
        RefreshToken storedRefreshToken = refreshTokenRepository.findByMember(member)
                .orElseThrow(() -> new Exception401("인증되지 않은 사용자입니다. 리프레시 토큰이 존재하지 않습니다."));
        if (!hashToken(refreshToken).equals(storedRefreshToken.getTokenValue())) {
            log.warn("리프레시 토큰 불일치. 사용자 ID: {}", memberId);
            throw new Exception401("토큰 정보가 일치하지 않습니다.");
        }

        // 신규 토큰 생성 및 DB 업데이트
        String newAccessToken = JwtUtil.createAccessToken(member);
        String newRefreshToken = JwtUtil.createRefreshToken(member);
        storedRefreshToken.updateTokenValue(hashToken(newRefreshToken));

        log.info("토큰 재발급 성공. 사용자 ID: {}", memberId);
        return new TokenReissueResponse(newAccessToken, newRefreshToken, JwtUtil.REFRESH_TOKEN_EXPIRATION_TIME / 1000);
    }

    // 로그인된 사용자의 비밀번호 변경
    @Transactional
    public void changePassword(Long memberId, ChangePasswordRequest request) {
        log.info("비밀번호 변경 시작. 사용자 ID: {}", memberId);
        Member member = memberService.findMember(memberId);
        // 현재 비밀번호 확인
        if (!passwordEncoder.matches(request.getCurrentPassword(), member.getPassword())) {
            throw new Exception401("현재 비밀번호가 일치하지 않습니다.");
        }
        // 새 비밀번호로 업데이트
        String newEncodedPassword = passwordEncoder.encode(request.getNewPassword());
        member.updatePassword(newEncodedPassword);
        log.info("비밀번호 변경 완료. 사용자 ID: {}", memberId);
    }

    // --- Private Helper Methods ---

    // 신규 소셜 회원 생성
    private Member createSocialMember(SocialLoginRequest request) {
        log.info("신규 소셜 회원 가입. Provider: {}, Email: {}", request.getProvider(), request.getEmail());
        if (memberRepository.existsByEmail(request.getEmail())) {
            throw new Exception400("이미 사용 중인 이메일입니다. 다른 로그인 방식을 시도해주세요.");
        }
        Member newMember = Member.builder()
                .email(request.getEmail())
                .provider(request.getProvider())
                .providerId(request.getProviderId())
                .role(Role.USER)
                .status(MemberStatus.ACTIVE)
                .build();
        MemberProfile newProfile = MemberProfile.builder().build();
        newMember.setMemberProfile(newProfile);
        return memberRepository.save(newMember);
    }

    // 계정 상태 (정지, 탈퇴 등) 검증
    private void checkAccountStatus(Member member) {
        if (member.getStatus() == MemberStatus.BANNED) {
            log.warn("정지된 계정 로그인 시도. 사용자 ID: {}", member.getId());
            throw new Exception401("활동이 정지된 계정입니다.");
        }
        if (member.getStatus() == MemberStatus.WITHDRAWN) {
            log.warn("탈퇴한 계정 로그인 시도. 사용자 ID: {}", member.getId());
            throw new Exception401("이미 탈퇴한 계정입니다.");
        }
        if (member.getStatus() != MemberStatus.ACTIVE) {
            log.error("처리되지 않은 계정 상태 로그인 시도. 사용자 ID: {}, 상태: {}", member.getId(), member.getStatus());
            throw new Exception401("로그인할 수 없는 계정 상태입니다.");
        }
    }

    // JWT 토큰 발급 및 응답 생성
    private LoginResponseWithTokens issueTokensAndGetResponse(Member member) {
        String accessToken = JwtUtil.createAccessToken(member);
        String refreshToken = JwtUtil.createRefreshToken(member);
        String hashedRefreshToken = hashToken(refreshToken);

        // Refresh Token을 DB에 저장 또는 업데이트
        refreshTokenRepository.findByMember(member)
                .ifPresentOrElse(
                        rt -> rt.updateTokenValue(hashedRefreshToken),
                        () -> refreshTokenRepository.save(RefreshToken.builder().member(member).tokenValue(hashedRefreshToken).build())
                );

        MemberLoginResponse responseDTO = new MemberLoginResponse(member);
        return new LoginResponseWithTokens(accessToken, refreshToken, JwtUtil.REFRESH_TOKEN_EXPIRATION_TIME / 1000, responseDTO);
    }

    // Refresh Token 보안을 위한 SHA-256 해싱
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
