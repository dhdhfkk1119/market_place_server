package com.market.market_place.members.dtos;

import com.market.market_place.members.domain.Member;
import com.market.market_place.members.domain.MemberProfile;
import com.market.market_place.members.domain.MemberStatus;
import lombok.Getter;

@Getter
public class MemberUpdateResponse {

    private final Long id;
    private final String name;
    private final MemberStatus status;
    private final String profileImageBase64; // 프로필 이미지 필드 추가

    /**
     * 수정된 Member 엔티티를 MemberUpdateResponse DTO로 변환합니다.
     * @param member 수정된 Member 엔티티
     */
    public MemberUpdateResponse(Member member) {
        this.id = member.getId();
        this.status = member.getStatus();

        MemberProfile profile = member.getMemberProfile();
        if (profile != null) {
            this.name = profile.getName();
            this.profileImageBase64 = profile.getProfileImageBase64(); // 프로필에서 Base64 이미지 가져오기
        } else {
            this.name = null;
            this.profileImageBase64 = null;
        }
    }
}
