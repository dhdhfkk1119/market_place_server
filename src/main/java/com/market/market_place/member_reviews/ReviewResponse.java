package com.market.market_place.member_reviews;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

public class ReviewResponse {
    @Data
    public static class ResponseDTO {
        private Long id;
        private String reviewerName;
        private Integer rating;
        private String comment;
        private LocalDateTime createdAt;

        @Builder
        public ResponseDTO(Review review) {
            this.id = review.getId();
            this.reviewerName = review.getReviewer().getMemberProfile().getName();
            this.rating = review.getRating();
            this.comment = review.getComment();
            this.createdAt = review.getCreatedAt();
        }
    }
}

