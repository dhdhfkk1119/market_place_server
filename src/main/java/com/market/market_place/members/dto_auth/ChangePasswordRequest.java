package com.market.market_place.members.dto_auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter // Controller에서 @RequestBody로 받기 위해 Setter 필요
public class ChangePasswordRequest {

    @NotBlank(message = "현재 비밀번호를 입력해주세요.")
    private String currentPassword;

    @NotBlank(message = "새 비밀번호를 입력해주세요.")
    @Pattern(
            regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@$!%*?&,.])[A-Za-z\\d@$!%*?&,.]{8,16}$",
            message = "비밀번호는 8~16자, 영문, 숫자, 특수문자를 모두 포함해야 합니다."
    )
    private String newPassword;
}
