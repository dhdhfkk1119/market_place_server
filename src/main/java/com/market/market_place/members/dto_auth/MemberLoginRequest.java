package com.market.market_place.members.dto_auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MemberLoginRequest {

    @NotBlank(message = "아이디 또는 이메일을 입력해주세요.")
    private String identity; // 아이디 또는 이메일

    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;
}
