package com.market.market_place.members.dto_auth;

import com.market.market_place.members.domain.Member;
import com.market.market_place.members.domain.MemberStatus;
import lombok.Getter;

@Getter
public class MemberLoginResponse {

    private final Long id;
    private final String loginId;
    private final String name;
    private final MemberStatus status; // 회원 상태 필드 추가

    /**
     * 로그인 성공 시 Member 엔티티를 사용하여 응답 DTO를 생성합니다.
     * @param member 인증된 Member 엔티티
     */
    public MemberLoginResponse(Member member) {
        this.id = member.getId();
        this.loginId = member.getLoginId();
        this.name = member.getMemberProfile() != null ? member.getMemberProfile().getName() : null;
        this.status = member.getStatus(); // 엔티티의 status 값을 DTO에 할당
    }
}
