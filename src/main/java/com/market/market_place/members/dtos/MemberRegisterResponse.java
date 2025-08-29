package com.market.market_place.members.dtos;

import com.market.market_place.members.domain.Member;
import com.market.market_place.members.domain.MemberStatus;
import lombok.Getter;

@Getter
public class MemberRegisterResponse {

    private final Long id;
    private final String loginId;
    private final String name;
    private final String role;
    private final MemberStatus status; // 회원 상태 필드 추가

    /**
     * Member 엔티티를 MemberRegisterResponse DTO로 변환합니다.
     * @param member 변환할 Member 엔티티
     */
    public MemberRegisterResponse(Member member) {
        this.id = member.getId();
        this.loginId = member.getLoginId();
        this.name = member.getMemberProfile() != null ? member.getMemberProfile().getName() : null; // 프로필에서 이름 가져오기
        this.role = member.getRole().name();
        this.status = member.getStatus(); // 엔티티의 status 값을 DTO에 할당
    }
}
