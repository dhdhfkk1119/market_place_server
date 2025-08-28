package com.market.market_place.community.community_category;

import com.market.market_place.community.community_topic.CommunityTopic;
import lombok.Data;

import java.util.List;

public class CommunityCategoryResponse {

    // 조회
    @Data
    public static class ListDTO {
        private Long id;
        private String name;

        public ListDTO(CommunityCategory category) {
            this.id = category.getId();
            this.name = category.getName();
        }
    }

    // 등록, 수정
    @Data
    public static class CategoryResponseDTO{
        private Long id;
        private String name;

        public CategoryResponseDTO(CommunityCategory category) {
            this.id = category.getId();
            this.name = category.getName();
        }
    }
}
