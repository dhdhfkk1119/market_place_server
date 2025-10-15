package com.market.market_place.community.community_category;

import com.market.market_place.community.community_topic.CommunityTopicResponse;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

public class CommunityCategoryResponse {

    @Data
    public static class ListDTO {
        private Long id;
        private String name;
        private List<CommunityTopicResponse.TopicResponseDTO> topics;

        @Builder
        public ListDTO(CommunityCategory category) {
            this.id = category.getId();
            this.name = category.getName();
            this.topics = category.getCommunityTopics().stream()
                    .map(CommunityTopicResponse.TopicResponseDTO::new)
                    .collect(Collectors.toList());
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
