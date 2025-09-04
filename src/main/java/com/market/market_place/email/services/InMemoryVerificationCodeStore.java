package com.market.market_place.email.services;

import com.market.market_place.email.VerificationPurpose;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component // 이 구현체를 Spring 빈으로 등록
public class InMemoryVerificationCodeStore implements VerificationCodeStore {

    private static final int EXPIRATION_MINUTES = 5; // 인증 코드 유효 시간 (5분)
    private final ConcurrentHashMap<String, VerificationInfo> verificationCodes = new ConcurrentHashMap<>();

    @Override
    public void storeCode(String email, VerificationPurpose purpose, String code) {
        String key = generateKey(email, purpose);
        verificationCodes.put(key, new VerificationInfo(code, LocalDateTime.now()));
        log.info("인증 코드 저장 완료 (메모리). 목적: {}, 이메일: {}", purpose, email);
    }

    @Override
    public boolean verifyCode(String email, VerificationPurpose purpose, String code) {
        String key = generateKey(email, purpose);
        log.debug("인증 코드 검증 시도 (메모리). 목적: {}, 이메일: {}", purpose, email);
        VerificationInfo info = verificationCodes.get(key);

        if (info == null) {
            log.warn("인증 코드 검증 실패: 코드가 존재하지 않음. 목적: {}, 이메일: {}", purpose, email);
            return false;
        }

        if (Duration.between(info.getCreatedAt(), LocalDateTime.now()).toMinutes() >= EXPIRATION_MINUTES) {
            log.warn("인증 코드 검증 실패: 유효 시간 만료. 목적: {}, 이메일: {}", purpose, email);
            verificationCodes.remove(key);
            return false;
        }

        if (!info.getCode().equals(code)) {
            log.warn("인증 코드 검증 실패: 코드가 일치하지 않음. 목적: {}, 이메일: {}", purpose, email);
            return false;
        }

        log.info("인증 코드 검증 성공 (메모리). 목적: {}, 이메일: {}", purpose, email);
        verificationCodes.remove(key);
        return true;
    }

    private String generateKey(String email, VerificationPurpose purpose) {
        return email + ":" + purpose.name();
    }

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
