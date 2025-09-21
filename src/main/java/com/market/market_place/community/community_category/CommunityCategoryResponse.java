package com.market.market_place.community.community_category;

import lombok.Builder;
import lombok.Data;

public class CommunityCategoryResponse {

    @Data
    public static class ListDTO {
        private Long id;
        private String name;

        @Builder
        public ListDTO(CommunityCategory category) {
            this.id = category.getId();
            this.name = category.getName();
        }
    }

    @Data
    public static class CategoryResponseDTO{
        private Long id;
        private String name;

        @Builder
        public CategoryResponseDTO(CommunityCategory category) {
            this.id = category.getId();
            this.name = category.getName();
        }
    }
}
