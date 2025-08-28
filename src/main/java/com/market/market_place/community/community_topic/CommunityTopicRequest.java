package com.market.market_place.community.community_topic;

import lombok.Data;

public class CommunityTopicRequest {

    // 저장
    @Data
    public static class SaveDTO{
        private String name;

        public CommunityTopic toEntity() {
            return CommunityTopic.builder()
                    .name(this.name)
                    .build();
        }
    }

    // 수정
    @Data
    public static class UpdateDTO {
        private String name;
    }
}
