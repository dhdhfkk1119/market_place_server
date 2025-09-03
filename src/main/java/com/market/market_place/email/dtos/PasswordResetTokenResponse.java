package com.market.market_place.email.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PasswordResetTokenResponse {
    private final String resetToken;
}
