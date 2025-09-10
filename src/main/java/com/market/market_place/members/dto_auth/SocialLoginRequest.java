package com.market.market_place.members.dto_auth;

import com.market.market_place.members.domain.Provider;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SocialLoginRequest {

    @NotNull(message = "소셜 로그인 제공자 정보는 필수입니다.")
    private Provider provider;

    @NotBlank(message = "Provider ID는 필수입니다.")
    private String providerId;

    @NotBlank(message = "이메일은 필수입니다.")
    @Email(message = "유효한 이메일 형식이 아닙니다.")
    private String email;
}
