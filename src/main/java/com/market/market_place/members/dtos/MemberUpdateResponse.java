package com.market.market_place.members.dtos;

import com.market.market_place.members.domain.Member;
import com.market.market_place.members.domain.MemberStatus;
import lombok.Getter;

@Getter
public class MemberUpdateResponse {

    private final Long id;
    private final String name;
    private final MemberStatus status; // 회원 상태 필드 추가

    /**
     * 수정된 Member 엔티티를 MemberUpdateResponse DTO로 변환합니다.
     * @param member 수정된 Member 엔티티
     */
    public MemberUpdateResponse(Member member) {
        this.id = member.getId();
        this.name = member.getMemberProfile() != null ? member.getMemberProfile().getName() : null; // 프로필에서 이름 가져오기
        this.status = member.getStatus(); // 엔티티의 status 값을 DTO에 할당
    }
}
