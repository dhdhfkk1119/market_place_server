package com.market.market_place.terms.dtos;

import com.market.market_place.terms.domain.Terms;
import lombok.Getter;

@Getter
public class TermsDto {
    private final Long id;
    private final String title;
    private final String content;
    private final boolean isRequired;

    public TermsDto(Terms terms) {
        this.id = terms.getId();
        this.title = terms.getTitle();
        this.content = terms.getContent();
        this.isRequired = terms.isRequired();
    }
}
