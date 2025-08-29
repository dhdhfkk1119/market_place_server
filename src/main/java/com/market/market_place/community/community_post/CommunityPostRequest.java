package com.market.market_place.community.community_post;

import com.market.market_place.community.community_topic.CommunityTopic;
import com.market.market_place.members.domain.Member;
import lombok.Data;

import java.util.List;

public class CommunityPostRequest {

    // 저장
    @Data
    public static class SaveDTO {
        private String title;
        private String content;
        private String location;
        private Long topicId;
        private List<String> images;

        public CommunityPost toEntity(Member member, CommunityTopic topic) {
            return CommunityPost.builder()
                    .title(this.title)
                    .content(this.content)
                    .location(this.location)
                    .topic(topic)
                    .member(member)
                    .build();
        }
    }

    // 수정
    @Data
    public static class UpdateDTO {
        private String title;
        private String content;
        private String location;
        private Long topicId;
        private List<String> images;
    }
}

