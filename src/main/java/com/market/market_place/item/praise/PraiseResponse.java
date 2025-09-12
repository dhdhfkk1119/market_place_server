package com.market.market_place.item.praise;

import lombok.Builder;
import lombok.Getter;
<<<<<<< HEAD
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;
=======
>>>>>>> f-board

@Getter
public class PraiseResponse {

<<<<<<< HEAD
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
=======
    private final String message;
    private final int updatedRetransactionRate;
    private final boolean success;

    @Builder
    public PraiseResponse(String message, int updatedRetransactionRate, boolean success) {
        this.message = message;
        this.updatedRetransactionRate = updatedRetransactionRate;
        this.success = success;
>>>>>>> f-board
    }
}
