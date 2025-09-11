package com.market.market_place.item.praise;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PraiseResponse {

    private String message;
    private double updatedMannerScore;
    private boolean isSuccess;
    private List<Long> praiseCategories;

    @Builder
    public PraiseResponse(String message, double updatedMannerScore, boolean isSuccess, List<Long> praiseCategories) {
        this.message = message;
        this.updatedMannerScore = updatedMannerScore;
        this.isSuccess = isSuccess;
        this.praiseCategories = praiseCategories;
    }
}