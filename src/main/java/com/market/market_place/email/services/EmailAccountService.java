package com.market.market_place.email.services;

import com.market.market_place._core._exception.Exception400;
import com.market.market_place._core._exception.Exception404;
import com.market.market_place._core._utils.JwtUtil;
import com.market.market_place.email.VerificationPurpose;
import com.market.market_place.email.dtos.ConfirmVerificationRequest;
import com.market.market_place.email.dtos.FindIdResponse;
import com.market.market_place.email.dtos.PasswordResetRequest;
import com.market.market_place.email.dtos.PasswordResetTokenResponse;
import com.market.market_place.email.dtos.SendPasswordResetCodeRequest;
import com.market.market_place.members.domain.Member;
import com.market.market_place.members.repositories.MemberRepository;
import com.market.market_place.members.services.MemberService;
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
public class EmailAccountService {

    private final MemberRepository memberRepository;
    private final EmailVerificationService emailVerificationService;
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    // 회원가입용 인증 코드 발송 요청 처리
    public void sendRegistrationCode(String email) {
        if (memberRepository.existsByEmail(email)) {
            throw new Exception400("이미 가입된 이메일입니다.");
        }
        emailVerificationService.sendCode(email, VerificationPurpose.REGISTER);
    }

    // 아이디 찾기를 위한 인증 코드를 이메일로 발송
    public void sendFindIdCode(String email) {
        log.info("아이디 찾기 코드 발송 요청. 이메일: {}", email);
        if (!memberRepository.existsByEmail(email)) {
            throw new Exception404("해당 이메일로 가입된 회원을 찾을 수 없습니다.");
        }
        emailVerificationService.sendCode(email, VerificationPurpose.FIND_ID);
    }

    // 이메일로 받은 인증 코드를 검증하고, 성공 시 마스킹 처리된 아이디를 반환
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

    // 비밀번호 재설정을 위한 인증 코드를 이메일로 발송합니다.
    public void sendPasswordResetCode(SendPasswordResetCodeRequest request) {
        log.info("비밀번호 재설정 코드 발송 요청. 로그인 ID: {}", request.getLoginId());
        Member member = memberRepository.findByLoginId(request.getLoginId())
                .orElseThrow(() -> new Exception404("해당 아이디를 가진 회원을 찾을 수 없습니다."));

        if (!Objects.equals(member.getEmail(), request.getEmail())) {
            throw new Exception400("아이디와 이메일 정보가 일치하지 않습니다.");
        }

        emailVerificationService.sendCode(request.getEmail(), VerificationPurpose.RESET_PASSWORD);
    }

    // 이메일로 받은 인증 코드를 검증하고, 성공 시 비밀번호 재설정용 임시 토큰을 발급합니다.
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

    // 발급받은 임시 토큰을 사용하여 최종적으로 비밀번호를 재설정합니다.
    @Transactional
    public void resetPassword(PasswordResetRequest request) {
        log.info("최종 비밀번호 재설정 요청.");
        JwtUtil.SessionUser sessionUser = JwtUtil.verifyPasswordResetToken(request.getResetToken());

        Member member = memberService.findMember(sessionUser.getId());
        String newEncodedPassword = passwordEncoder.encode(request.getNewPassword());
        member.updatePassword(newEncodedPassword);
        log.info("최종 비밀번호 재설정 완료. 사용자 ID: {}", member.getId());
    }

    private String maskLoginId(String loginId) {
        if (loginId == null || loginId.length() <= 3) {
            return loginId;
        }
        return loginId.substring(0, 3) + "***";
    }
}
