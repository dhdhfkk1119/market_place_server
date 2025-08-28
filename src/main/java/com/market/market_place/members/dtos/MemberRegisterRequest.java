package com.market.market_place.members.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;
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

    // 비밀번호 (8자 이상 20자 이하)
    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Size(min = 8, max = 20, message = "비밀번호는 8자 이상 20자 이하로 입력해주세요.")
    private String password;

    // 전화번호 (10자리 또는 11자리 숫자)
    @NotBlank(message = "전화번호를 입력해주세요.")
    @Pattern(regexp = "^\\d{10,11}$", message = "전화번호는 10자리 또는 11자리 숫자여야 합니다.")
    private String phoneNumber;

    // 전화번호 인증 여부
    @NotNull(message = "전화번호 인증 여부가 필요합니다.")
    private Boolean isVerified;

    // 동의한 약관 ID 목록
    @NotNull(message = "약관 동의가 필요합니다.")
    private Set<Long> agreedTermIds;
}
