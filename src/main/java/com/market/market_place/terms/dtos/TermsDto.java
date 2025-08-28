package com.market.market_place.terms.dtos;

import com.market.market_place.terms.domain.Terms;
import lombok.Getter;

@Getter
public class TermsDto {
    private final Long id;
    private final String title;
    private final String content;
    private final boolean isRequired;

    public TermsDto(Terms term) {
        this.id = term.getId();
        this.title = term.getTitle();
        this.content = term.getContent();
        this.isRequired = term.isRequired();
    }
}
