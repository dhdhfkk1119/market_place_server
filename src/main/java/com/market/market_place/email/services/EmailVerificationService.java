package com.market.market_place.email.services;

import com.market.market_place.email.VerificationPurpose;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailVerificationService {

    private final EmailService emailService;
    private final VerificationCodeStore verificationCodeStore;

    /**
     * 주어진 목적에 맞는 인증 코드를 생성하고 이메일로 발송합니다.
     * 이메일 중복 확인과 같은 비즈니스 규칙 검증은 이 메서드를 호출하는 상위 서비스의 책임입니다.
     *
     * @param email   인증 코드를 받을 이메일 주소
     * @param purpose 인증 목적 (회원가입, 비밀번호 재설정 등)
     */
    public void sendCode(String email, VerificationPurpose purpose) {
        log.info("{} 목적의 이메일 인증 코드 발송 절차 시작. 수신자: {}", purpose, email);
        String subject = getSubjectForPurpose(purpose);
        String code = generate6DigitCode();
        log.info("개발용 인증 코드 (목적: {}, 이메일: {}): {}", purpose, email, code);

        verificationCodeStore.storeCode(email, purpose, code);

        String text = "인증 코드는 [" + code + "] 입니다. 5분 이내에 입력해주세요.";
        emailService.sendEmail(email, subject, text);
        log.info("{} 목적의 이메일 인증 코드 발송 절차 완료. 수신자: {}", purpose, email);
    }

    /**
     * 주어진 목적의 인증 코드가 유효한지 검증합니다.
     *
     * @param email   사용자의 이메일 주소
     * @param purpose 검증할 인증 목적
     * @param code    사용자가 입력한 인증 코드
     * @return 코드가 유효하면 true, 그렇지 않으면 false
     */
    public boolean verifyCode(String email, VerificationPurpose purpose, String code) {
        boolean isVerified = verificationCodeStore.verifyCode(email, purpose, code);
        log.info("이메일 코드 검증 요청 처리. 목적: {}, 이메일: {}, 결과: {}", purpose, email, isVerified);
        return isVerified;
    }

    // --- Private Helper Methods ---

    private String getSubjectForPurpose(VerificationPurpose purpose) {
        return switch (purpose) {
            case REGISTER -> "[Market Place] 회원가입 이메일 인증 코드";
            case RESET_PASSWORD -> "[Market Place] 비밀번호 재설정 이메일 인증 코드";
            case FIND_ID -> "[Market Place] 아이디 찾기 이메일 인증 코드";
        };
    }

    private String generate6DigitCode() {
        Random random = new Random();
        int number = 100000 + random.nextInt(900000); // 100000 ~ 999999
        return String.valueOf(number);
    }
}
