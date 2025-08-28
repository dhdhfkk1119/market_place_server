package com.market.market_place.community.community_topic;

import lombok.Data;

public class CommunityTopicResponse {

    // 조회
    @Data
    public static class ListDTO {
        private Long id;
        private String name;

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

        public TopicResponseDTO(CommunityTopic topic) {
            this.id = topic.getId();
            this.name = topic.getName();
        }
    }
}
