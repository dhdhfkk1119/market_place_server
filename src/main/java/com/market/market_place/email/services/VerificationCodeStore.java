package com.market.market_place.email.services;

import com.market.market_place.email.VerificationPurpose;

/**
 * 인증 코드의 저장 및 검증을 담당하는 저장소의 역할을 정의하는 인터페이스입니다.
 * 이 인터페이스를 통해 메모리, Redis, DB 등 다양한 저장소 구현체를 사용할 수 있습니다.
 */
public interface VerificationCodeStore {

    /**
     * 주어진 이메일과 목적에 맞는 인증 코드를 저장합니다.
     *
     * @param email   사용자의 이메일 주소
     * @param purpose 인증 목적 (예: 회원가입, 비밀번호 재설정)
     * @param code    저장할 인증 코드
     */
    void storeCode(String email, VerificationPurpose purpose, String code);

    /**
     * 주어진 이메일과 목적에 맞는 인증 코드가 유효한지 검증합니다.
     *
     * @param email   사용자의 이메일 주소
     * @param purpose 검증할 인증 목적
     * @param code    사용자가 입력한 인증 코드
     * @return 코드가 유효하면 true, 그렇지 않으면 false
     */
    boolean verifyCode(String email, VerificationPurpose purpose, String code);
}
