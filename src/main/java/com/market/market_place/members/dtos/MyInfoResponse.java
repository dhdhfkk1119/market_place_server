package com.market.market_place.members.dtos;

import com.market.market_place.members.domain.Member;
import com.market.market_place.members.domain.MemberProfile;
import com.market.market_place.members.domain.MemberStatus;
import lombok.Getter;

@Getter
public class MyInfoResponse {

    private final Long id;
    private final String loginId;
    private final String email;
    private final String name;
    private final String role;
    private final MemberStatus status;
    private final String profileImageBase64;

    public MyInfoResponse(Member member) {
        this.id = member.getId();
        this.loginId = member.getLoginId();
        this.role = member.getRole().name();
        this.status = member.getStatus();
        this.email = member.getEmail();

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
