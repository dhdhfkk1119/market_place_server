package com.market.market_place.community.community_topic;

import lombok.Builder;
import lombok.Data;

public class CommunityTopicResponse {

    // 조회
    @Data
    public static class ListDTO {
        private Long id;
        private String name;

        @Builder
        public ListDTO(CommunityTopic topic) {
            this.id = topic.getId();
            this.name = topic.getName();
        }
    }

    // 등록, 수정
    @Data
    public static class TopicResponseDTO{
        private Long id;
        private String name;

        @Builder
        public TopicResponseDTO(CommunityTopic topic) {
            this.id = topic.getId();
            this.name = topic.getName();
        }
    }
}
