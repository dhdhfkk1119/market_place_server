package com.market.market_place.community.community_comment_like;

import lombok.Data;

public class CommunityCommentLikeResponse {

    @Data
    public static class ResponseDTO{
        private Boolean liked;
        private Long likeCount;

        public ResponseDTO(Boolean liked, Long likeCount) {
            this.liked = liked;
            this.likeCount = likeCount;
        }
    }
}
