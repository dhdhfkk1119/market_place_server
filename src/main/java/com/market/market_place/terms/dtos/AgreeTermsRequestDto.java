package com.market.market_place.terms.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class AgreeTermsRequestDto {
    private Set<Long> agreedTermIds;
}
