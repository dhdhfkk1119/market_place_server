package com.market.market_place.email.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PasswordResetRequest {

    @NotBlank(message = "비밀번호 재설정 토큰이 필요합니다.")
    private String resetToken;

    @NotBlank(message = "새 비밀번호를 입력해주세요.")
    @Pattern(
            regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@$!%*?&,.])[A-Za-z\\d@$!%*?&,.]{8,16}$",
            message = "비밀번호는 8~16자, 영문, 숫자, 특수문자를 모두 포함해야 합니다."
    )
    private String newPassword;
}
