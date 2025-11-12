package markit.item.praise;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class PraiseResponse {

    private String message;
    private double updatedMannerScore;
    private boolean isSuccess;
    private List<Long> praiseCategories;
    private double updatedRetransactionRate;

    @Builder
    public PraiseResponse(String message, double updatedMannerScore, boolean isSuccess, List<Long> praiseCategories, double updatedRetransactionRate) {
        this.message = message;
        this.updatedMannerScore = updatedMannerScore;
        this.isSuccess = isSuccess;
        this.praiseCategories = praiseCategories;
        this.updatedRetransactionRate = updatedRetransactionRate;
    }

    public static PraiseResponse success(String msg, double mannerScore, int reRate, List<Long> categories) {
        return PraiseResponse.builder()
                .message(msg)
                .isSuccess(true)
                .updatedMannerScore(mannerScore)
                .updatedRetransactionRate(reRate)
                .praiseCategories(categories)
                .build();
    }

    public static PraiseResponse alreadyPraised() {
        return PraiseResponse.builder()
                .message("이미 해당 거래를 칭찬 하셨습니다")
                .isSuccess(false)
                .build();
    }
}