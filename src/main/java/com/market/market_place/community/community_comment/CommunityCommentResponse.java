package com.market.market_place.community.community_comment;

import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;


public class CommunityCommentResponse {

    @Data
    public static class ResponseDTO{
        private Long id;
        private String content;
        private String writerName;
        private Timestamp createdAt;

        @Builder
        public ResponseDTO(CommunityComment comment) {
            this.id = comment.getId();
            this.content = comment.getContent();
            this.writerName = comment.getMember().getMemberProfile().getName();
            this.createdAt = comment.getCreatedAt();
        }
    }
}
