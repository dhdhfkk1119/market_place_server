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

    // --- 아이디 찾기 V2 (신규 로직) ---

    /**
     * 이메일로 사용자를 찾아 마스킹된 아이디를 반환합니다.
     * @param email 사용자 이메일
     * @return 마스킹된 아이디가 담긴 응답 DTO
     */
    public FindIdResponse getMaskedLoginId(String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new Exception404("해당 이메일로 가입된 회원을 찾을 수 없습니다."));
        String maskedLoginId = maskLoginId(member.getLoginId());
        return new FindIdResponse(maskedLoginId);
    }

    /**
     * 이메일로 사용자를 찾아 해당 이메일 주소로 전체 아이디를 발송합니다.
     * @param email 사용자 이메일
     */
    public void sendFullLoginIdToEmail(String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new Exception404("해당 이메일로 가입된 회원을 찾을 수 없습니다."));
        // EmailVerificationService에 아이디 전송을 위한 새 메서드를 호출 (추가 구현 필요)
        emailVerificationService.sendLoginId(email, member.getLoginId());
        log.info("전체 아이디 이메일 발송 완료. 이메일: {}, 아이디: {}", email, member.getLoginId());
    }

    // --- 회원가입 ---
    public void sendRegistrationCode(String email) {
        if (memberRepository.existsByEmail(email)) {
            throw new Exception400("이미 가입된 이메일입니다.");
        }
        emailVerificationService.sendCode(email, VerificationPurpose.REGISTER);
    }

    // --- 비밀번호 재설정 ---
    public void sendPasswordResetCode(SendPasswordResetCodeRequest request) {
        log.info("비밀번호 재설정 코드 발송 요청. 로그인 ID: {}", request.getLoginId());
        Member member = memberRepository.findByLoginId(request.getLoginId())
                .orElseThrow(() -> new Exception404("해당 아이디를 가진 회원을 찾을 수 없습니다."));

        if (!Objects.equals(member.getEmail(), request.getEmail())) {
            throw new Exception400("아이디와 이메일 정보가 일치하지 않습니다.");
        }

        emailVerificationService.sendCode(request.getEmail(), VerificationPurpose.RESET_PASSWORD);
    }

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
