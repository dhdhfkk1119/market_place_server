package com.market.market_place.members.dto_auth;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MemberLoginRequest {

    private String loginId;
    private String password;
}
