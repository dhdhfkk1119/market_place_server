package com.market.market_place.email.services;

import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class VerificationCodeManager {

    private static final int EXPIRATION_MINUTES = 5; // 인증 코드 유효 시간 (5분)
    private final ConcurrentHashMap<String, VerificationInfo> verificationCodes = new ConcurrentHashMap<>();

    // 인증 코드 저장
    public void storeCode(String email, String code) {
        verificationCodes.put(email, new VerificationInfo(code, LocalDateTime.now()));
    }

    // 인증 코드 검증
    public boolean verifyCode(String email, String code) {
        VerificationInfo info = verificationCodes.get(email);

        // 1. 코드가 존재하지 않는 경우
        if (info == null) {
            return false;
        }

        // 2. 유효 시간이 만료된 경우
        if (Duration.between(info.getCreatedAt(), LocalDateTime.now()).toMinutes() >= EXPIRATION_MINUTES) {
            verificationCodes.remove(email); // 만료된 코드는 맵에서 제거
            return false;
        }

        // 3. 코드가 일치하는 경우
        if (info.getCode().equals(code)) {
            verificationCodes.remove(email); // 성공적으로 검증된 코드는 맵에서 제거
            return true;
        }

        // 4. 코드가 일치하지 않는 경우
        return false;
    }

    // 인증 코드와 생성 시간을 저장하는 내부 클래스
    private static class VerificationInfo {
        private final String code;
        private final LocalDateTime createdAt;

        public VerificationInfo(String code, LocalDateTime createdAt) {
            this.code = code;
            this.createdAt = createdAt;
        }

        public String getCode() {
            return code;
        }

        public LocalDateTime getCreatedAt() {
            return createdAt;
        }
    }
}
