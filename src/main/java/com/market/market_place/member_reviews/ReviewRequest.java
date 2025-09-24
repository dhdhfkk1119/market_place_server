package com.market.market_place.member_reviews;

import lombok.Data;

public class ReviewRequest {
    @Data
    public static class SaveDTO {
        private Long reviewedId;
        private Integer rating;
        private String comment;
    }

    @Data
    public static class UpdateDTO {
        private Integer rating;
        private String comment;
    }
}
