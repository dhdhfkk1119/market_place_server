package com.market.market_place.community.community_topic;

import com.market.market_place.community.community_category.CommunityCategory;
import lombok.Data;

public class CommunityTopicRequest {

    // 저장
    @Data
    public static class SaveDTO{
        private String name;
        private Long categoryId;

        public CommunityTopic toEntity(CommunityCategory category) {
            return CommunityTopic.builder()
                    .name(this.name)
                    .category(category)
                    .build();
        }
    }

    // 수정
    @Data
    public static class UpdateDTO {
        private String name;
        private Long categoryId;
    }
}
