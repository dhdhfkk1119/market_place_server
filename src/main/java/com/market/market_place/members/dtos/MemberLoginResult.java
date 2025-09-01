package com.market.market_place.members.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberLoginResult {
    private final String token;
    private final MemberLoginResponse loginResponse;
}
