package com.market.market_place.item.review;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class TradeReviewResponse {

    private Long id;
    private String content;
    private double rating;
    private String reviewerLoginId;
    private LocalDateTime createdAt;

    public static TradeReviewResponse from(TradeReview tradeReview) {
        return new TradeReviewResponse(
                tradeReview.getId(),
                tradeReview.getContent(),
                tradeReview.getRating(),
                tradeReview.getReviewer().getLoginId(),
                tradeReview.getCreatedAt()
        );
    }
}