package com.market.market_place.email.services;

import com.market.market_place._core._exception.Exception400;
import com.market.market_place.members.repositories.MemberAuthRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class EmailVerificationService {

    private final EmailService emailService;
    private final VerificationCodeManager verificationCodeManager;
    private final MemberAuthRepository memberAuthRepository;

    // 인증 코드 발송
    public void sendVerificationCode(String email) {
        // 1. 이메일 중복 확인
        if (memberAuthRepository.findByEmail(email).isPresent()) {
            throw new Exception400("이미 가입된 이메일입니다.");
        }

        // 2. 6자리 인증 코드 생성
        String code = generate6DigitCode();

        // 3. 인증 코드 임시 저장
        verificationCodeManager.storeCode(email, code);

        // 4. 이메일 발송
        String subject = "[Market Place] 회원가입 이메일 인증 코드";
        String text = "인증 코드는 [" + code + "] 입니다. 5분 이내에 입력해주세요.";
        emailService.sendEmail(email, subject, text);
    }

    // 인증 코드 검증
    public boolean verifyCode(String email, String code) {
        return verificationCodeManager.verifyCode(email, code);
    }

    // 6자리 숫자 코드 생성기
    private String generate6DigitCode() {
        Random random = new Random();
        int number = 100000 + random.nextInt(900000); // 100000 ~ 999999
        return String.valueOf(number);
    }
}
