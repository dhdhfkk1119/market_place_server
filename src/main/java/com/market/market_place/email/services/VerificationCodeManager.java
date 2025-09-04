package com.market.market_place.email.services;

import com.market.market_place.email.VerificationPurpose;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class VerificationCodeManager {

    private static final int EXPIRATION_MINUTES = 5; // 인증 코드 유효 시간 (5분)
    private final ConcurrentHashMap<String, VerificationInfo> verificationCodes = new ConcurrentHashMap<>();

    // 인증 코드 저장 (목적 추가)
    public void storeCode(String email, VerificationPurpose purpose, String code) {
        String key = generateKey(email, purpose);
        verificationCodes.put(key, new VerificationInfo(code, LocalDateTime.now()));
        log.info("인증 코드 저장 완료. 목적: {}, 이메일: {}", purpose, email);
    }

    // 인증 코드 검증 (목적 추가)
    public boolean verifyCode(String email, VerificationPurpose purpose, String code) {
        String key = generateKey(email, purpose);
        log.debug("인증 코드 검증 시도. 목적: {}, 이메일: {}", purpose, email);
        VerificationInfo info = verificationCodes.get(key);

        // 1. 코드가 존재하지 않는 경우
        if (info == null) {
            log.warn("인증 코드 검증 실패: 코드가 존재하지 않음. 목적: {}, 이메일: {}", purpose, email);
            return false;
        }

        // 2. 유효 시간이 만료된 경우
        if (Duration.between(info.getCreatedAt(), LocalDateTime.now()).toMinutes() >= EXPIRATION_MINUTES) {
            log.warn("인증 코드 검증 실패: 유효 시간 만료. 목적: {}, 이메일: {}", purpose, email);
            verificationCodes.remove(key); // 만료된 코드는 맵에서 제거
            return false;
        }

        // 3. 코드가 일치하지 않는 경우
        if (!info.getCode().equals(code)) {
            log.warn("인증 코드 검증 실패: 코드가 일치하지 않음. 목적: {}, 이메일: {}", purpose, email);
            return false;
        }

        // 4. 코드가 일치하는 경우
        log.info("인증 코드 검증 성공. 목적: {}, 이메일: {}", purpose, email);
        verificationCodes.remove(key); // 성공적으로 검증된 코드는 맵에서 제거
        return true;
    }

    // 이메일과 목적을 조합하여 고유한 키를 생성하는 헬퍼 메서드
    private String generateKey(String email, VerificationPurpose purpose) {
        return email + ":" + purpose.name();
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
