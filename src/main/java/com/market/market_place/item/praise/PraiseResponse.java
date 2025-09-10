package com.market.market_place.item.praise;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PraiseResponse {

    private String message;
    private int updatedRetransactionRate;
    private boolean isSuccess;

    @Builder
    public PraiseResponse(String message, int updatedRetransactionRate, boolean isSuccess) {
        this.message = message;
        this.updatedRetransactionRate = updatedRetransactionRate;
        this.isSuccess = isSuccess;
    }
}