package com.market.market_place.members.dto_profile;

import com.market.market_place.members.domain.Member;
import com.market.market_place.members.domain.MemberProfile;
import com.market.market_place.members.domain.MemberStatus;
import com.market.market_place.members.domain.Role;
import com.market.market_place.members.domain.Provider;
import lombok.Getter;

@Getter
public class MemberUpdateResponse {

    private final Long id;
    private final Role role;
    private final Provider provider;
    private final MemberStatus status;
    private final String name;
    private final String profileImageBase64;

    /**
     * 수정된 Member 엔티티를 MemberUpdateResponse DTO로 변환합니다.
     * @param member 수정된 Member 엔티티
     */
    public MemberUpdateResponse(Member member) {
        this.id = member.getId();
        this.role = member.getRole();
        this.provider = member.getProvider();
        this.status = member.getStatus();

        MemberProfile profile = member.getMemberProfile();
        if (profile != null) {
            this.name = profile.getName();
            this.profileImageBase64 = profile.getProfileImageBase64();
        } else {
            this.name = null;
            this.profileImageBase64 = null;
        }
    }
}
