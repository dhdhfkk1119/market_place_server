package com.market.market_place.item.review;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class TradeReviewRequest {

    @NotBlank(message = "후기 내용은 필수입니다")
    @Size(max = 500, message = "후기 내용은 500자를 초과할 수 없습니다")
    private String content;

    @Min(value = 1, message = "평점은 1점 이상이어야 합니다")
    @Max(value = 5, message = "평점은 5점 이하이어야 합니다")
    private double rating;

    public TradeReview toEntity() {
        return TradeReview.builder()
                .content(this.content)
                .rating(this.rating)
                .build();
    }
}
