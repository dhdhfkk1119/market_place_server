package com.market.market_place.item.review;


import com.market.market_place.item.status.Trade;
import com.market.market_place.members.domain.Member;
import jakarta.validation.constraints.*;
import lombok.Data;

// 거래 리뷰 요청 DTO (작성/수정 요청)
@Data
public class TradeReviewRequest {

    @NotNull(message = "거래 ID는 필수입니다.")
    private Long tradeId; // 거래 ID

    @NotBlank(message = "후기 내용은 필수입니다")
    @Size(max = 100, message = "후기 내용은 500자를 초과할 수 없습니다")
    private String content; // 리뷰 내용

    @Min(value = 1, message = "평점은 1점 이상이어야 합니다")
    @Max(value = 5, message = "평점은 5점 이하이어야 합니다")
    private double rating; // 리뷰 평점

    // DTO를 엔티티로 변환
    public TradeReview toEntity(Trade trade, Member reviewer) {
        return TradeReview.builder()
                .trade(trade)
                .reviewer(reviewer)
                .content(this.content)
                .rating(this.rating)
                .build();
    }
}
