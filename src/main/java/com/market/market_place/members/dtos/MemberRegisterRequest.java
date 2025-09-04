package com.market.market_place.members.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

// 회원가입 요청을 위한 DTO
@Getter
@Setter
@NoArgsConstructor
public class MemberRegisterRequest {

    // 로그인 아이디 (4자 이상 20자 이하)
    @NotBlank(message = "아이디를 입력해주세요.")
    @Size(min = 4, max = 20, message = "아이디는 4자 이상 20자 이하로 입력해주세요.")
    private String loginId;

    // 비밀번호 (8자 이상 20자 이하, 영문, 숫자, 특문)
    @NotBlank(message = "새 비밀번호를 입력해주세요.")
    @Pattern(
            regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@$!%*?&,.])[A-Za-z\\d@$!%*?&,.]{8,16}$",
            message = "비밀번호는 8~16자, 영문, 숫자, 특수문자를 모두 포함해야 합니다."
    )
    private String password;

    // 이메일
    @NotBlank(message = "이메일을 입력해주세요.")
    @Email(message = "유효한 이메일 주소를 입력해주세요.")
    private String email;

    // 주소 (선택 사항)
    private String address;

    // 이메일 인증 여부
    @NotNull(message = "이메일 인증 여부가 필요합니다.")
    private Boolean isEmailVerified;

    // 동의한 약관 ID 목록
    @NotNull(message = "약관 동의가 필요합니다.")
    private Set<Long> agreedTermIds;
}
