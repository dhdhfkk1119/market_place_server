package com.market.market_place.community.community_category;

import lombok.Data;

public class CommunityCategoryRequest {

    @Data
    public static class SaveDTO {
        private String name;

        public CommunityCategory toEntity() {
            return  CommunityCategory.builder()
                    .name(this.name)
                    .build();
        }
    }

    @Data
    public static class UpdateDTO {
        private String name;

    }
}
