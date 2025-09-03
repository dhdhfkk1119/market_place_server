package com.market.market_place.email;

// 이메일 인증의 목적을 구분하기 위한 열거형
public enum VerificationPurpose {
    REGISTER, // 회원가입
    FIND_ID, // 아이디 찾기
    RESET_PASSWORD // 비밀번호 재설정
}
