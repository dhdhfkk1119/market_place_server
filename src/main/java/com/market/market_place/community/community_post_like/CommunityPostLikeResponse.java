package com.market.market_place.community.community_post_like;


import lombok.Data;

public class CommunityPostLikeResponse {

    @Data
    public static class ResponseDTO {
        private Boolean liked;
        private Long likeCount;

        public ResponseDTO(Boolean liked, Long likeCount) {
            this.liked = liked;
            this.likeCount = likeCount;
        }
    }
}
