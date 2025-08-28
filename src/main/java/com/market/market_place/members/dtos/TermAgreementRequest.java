package com.market.market_place.members.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

// 약관 동의 요청을 위한 DTO
@Getter
@Setter
@NoArgsConstructor
public class TermAgreementRequest {

    // 동의한 약관 ID 목록
    @NotNull(message = "동의한 약관 목록은 null일 수 없습니다.")
    @NotEmpty(message = "최소 하나 이상의 약관에 동의해야 합니다.")
    private Set<Long> agreedTermIds;
}
