package com.market.market_place.item.praise;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class PraiseResponse {

    private String message;
    private double updatedMannerScore;
    private boolean isSuccess;
    private List<Long> praiseCategories;
    private int updatedRetransactionRate;

    @Builder
    public PraiseResponse(String message, double updatedMannerScore, boolean isSuccess, List<Long> praiseCategories, int updatedRetransactionRate) {
        this.message = message;
        this.updatedMannerScore = updatedMannerScore;
        this.isSuccess = isSuccess;
        this.praiseCategories = praiseCategories;
        this.updatedRetransactionRate = updatedRetransactionRate;
    }
}