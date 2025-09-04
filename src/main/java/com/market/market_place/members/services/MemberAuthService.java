package com.market.market_place.members.services;

import com.market.market_place._core._exception.Exception400;
import com.market.market_place._core._exception.Exception401;
import com.market.market_place._core._exception.Exception404;
import com.market.market_place._core._utils.JwtUtil;
import com.market.market_place.email.VerificationPurpose;
import com.market.market_place.email.dtos.*;
import com.market.market_place.email.services.EmailVerificationService;
import com.market.market_place.members.domain.Member;
import com.market.market_place.members.domain.MemberStatus;
import com.market.market_place.members.dtos.*;
import com.market.market_place.members.repositories.MemberRepository;
import com.market.market_place.terms.dtos.AgreeTermsRequestDto;
import com.market.market_place.terms.services.TermsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberAuthService {

    private final MemberRepository memberRepository;
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
    public MemberLoginResult login(MemberLoginRequest request) {
        log.info("로그인 시도. 로그인 ID: {}", request.getLoginId());
        Member member = memberRepository.findByLoginId(request.getLoginId())
                                        .orElseThrow(() -> new Exception401("아이디 또는 비밀번호가 일치하지 않습니다."));
        if (member.getStatus() != MemberStatus.ACTIVE) {
            log.warn("비활성 계정 로그인 시도. 사용자 ID: {}, 상태: {}", member.getId(), member.getStatus());
            throw new Exception401("이미 탈퇴했거나 정지된 계정입니다.");
        }
        if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            log.warn("비밀번호 불일치. 로그인 ID: {}", request.getLoginId());
            throw new Exception401("아이디 또는 비밀번호가 일치하지 않습니다.");
        }
        String jwt = JwtUtil.createToken(member);
        MemberLoginResponse responseDTO = new MemberLoginResponse(member);
        log.info("로그인 성공. 사용자 ID: {}", member.getId());
        return new MemberLoginResult(jwt, responseDTO);
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
}
