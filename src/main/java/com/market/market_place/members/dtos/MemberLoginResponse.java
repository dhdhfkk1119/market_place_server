package com.market.market_place.members.dtos;

import com.market.market_place.members.domain.Member;
import lombok.Getter;

@Getter
public class MemberLoginResponse {

    private final Long id;
    private final String loginId;
    private final String name;
    private final String token;

    /**
     * 로그인 성공 시 Member 엔티티와 토큰을 사용하여 응답 DTO를 생성합니다.
     * @param member 인증된 Member 엔티티
     * @param token  발급된 JWT 토큰
     */
    public MemberLoginResponse(Member member, String token) {
        this.id = member.getId();
        this.loginId = member.getLoginId();
        this.name = member.getMemberProfile() != null ? member.getMemberProfile().getName() : null;
        this.token = token;
    }
}
