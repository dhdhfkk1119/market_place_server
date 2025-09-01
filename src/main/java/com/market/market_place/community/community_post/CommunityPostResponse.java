package com.market.market_place.community.community_post;

import com.market.market_place.community.community_comment.CommunityComment;
import com.market.market_place.community.community_post_image.CommunityPostImage;
import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

public class CommunityPostResponse {

    // 전체조회
    @Data
    public static class ListDTO{
        private Long id;
        private String title;
        private String topic;
        private String location;
        private String preview;
        private String thumbnail;
        private int likeCount;
        private int viewCount;
        private Timestamp createdAt;

        @Builder
        public ListDTO(CommunityPost post) {
            this.id = post.getId();
            this.title = post.getTitle();
            this.topic = post.getTopic().getName();
            this.location = post.getLocation();
            this.likeCount = post.getLikeCount();
            this.viewCount = post.getViewCount();
            this.createdAt = post.getCreatedAt();
            this.thumbnail = post.getImages().isEmpty() ?
                    null : post.getImages().get(0).getImageUrl(); // 첫번째 이미지만 보임

            // 내용 30줄 미리보기
            String content = post.getContent();
            this.preview = content != null && content.length() > 30
                    ? content.substring(0, 30) + "..." : content;
        }
    }

    // 상세조회
    @Data
    public static class DetailDTO {
        private Long id;
        private String title;
        private String content;
        private String writerName;
        private String topic;
        private int likeCount;
        private int viewCount;
        private Timestamp createdAt;
        private String location;
        private List<String> images;
        private List<CommunityComment> comments;

        @Builder
        public DetailDTO(CommunityPost post) {
            this.id = post.getId();
            this.title = post.getTitle();
            this.content = post.getContent();
            this.writerName = post.getMember().getMemberProfile().getName();
            this.topic = post.getTopic().getName();
            this.likeCount = post.getLikeCount();
            this.viewCount = post.getViewCount();
            this.createdAt = post.getCreatedAt();
            this.location = post.getLocation();
            this.images = post.getImages().stream()
                    .map(CommunityPostImage::getImageUrl).collect(Collectors.toList());
            this.comments = post.getComments();
        }
    }

    // 작성, 수정
    @Data
    public static class ResponseDTO{
        private Long id;
        private String title;
        private String content;

        @Builder
        public ResponseDTO(CommunityPost post) {
            this.id = post.getId();
            this.title = post.getTitle();
            this.content = post.getContent();
        }
    }
}
