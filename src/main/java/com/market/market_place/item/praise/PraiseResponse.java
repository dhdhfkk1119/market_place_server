package com.market.market_place.item.praise;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PraiseResponse {

    private final String message;
    private final int updatedRetransactionRate;
    private final boolean success;

    @Builder
    public PraiseResponse(String message, int updatedRetransactionRate, boolean success) {
        this.message = message;
        this.updatedRetransactionRate = updatedRetransactionRate;
        this.success = success;
    }
}
