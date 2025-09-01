package com.market.market_place.email.services;

import com.market.market_place._core._exception.Exception400;
import com.market.market_place.members.repositories.MemberAuthRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Random;

@Slf4j // 로깅을 위한 어노테이션 추가
@Service
@RequiredArgsConstructor
public class EmailVerificationService {

    private final EmailService emailService;
    private final VerificationCodeManager verificationCodeManager;
    private final MemberAuthRepository memberAuthRepository;

    // 인증 코드 발송
    public void sendVerificationCode(String email) {
        log.info("이메일 인증 코드 발송 절차 시작. 수신자: {}", email);
        // 1. 이메일 중복 확인
        if (memberAuthRepository.findByEmail(email).isPresent()) {
            throw new Exception400("이미 가입된 이메일입니다.");
        }

        // 2. 6자리 인증 코드 생성
        String code = generate6DigitCode();

        // 3. 개발/테스트 환경을 위한 인증 코드 로깅
        log.info("개발용 인증 코드 ({}): {}", email, code);

        // 4. 인증 코드 임시 저장
        verificationCodeManager.storeCode(email, code);

        // 5. 이메일 발송
        String subject = "[Market Place] 회원가입 이메일 인증 코드";
        String text = "인증 코드는 [" + code + "] 입니다. 5분 이내에 입력해주세요.";
        emailService.sendEmail(email, subject, text);
        log.info("이메일 인증 코드 발송 절차 완료. 수신자: {}", email);
    }

    // 인증 코드 검증
    public boolean verifyCode(String email, String code) {
        boolean isVerified = verificationCodeManager.verifyCode(email, code);
        log.info("이메일 코드 검증 요청 처리. 이메일: {}, 결과: {}", email, isVerified);
        return isVerified;
    }

    // 6자리 숫자 코드 생성기
    private String generate6DigitCode() {
        Random random = new Random();
        int number = 100000 + random.nextInt(900000); // 100000 ~ 999999
        return String.valueOf(number);
    }
}
