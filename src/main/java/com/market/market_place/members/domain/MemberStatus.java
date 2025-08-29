package com.market.market_place.members.domain;

public enum MemberStatus {
    ACTIVE,     // 활성 (정상 활동)
    WITHDRAWN,  // 탈퇴 (사용자가 자발적으로 탈퇴)
    BANNED      // 정지 (관리자에 의해 이용 제한)
}
