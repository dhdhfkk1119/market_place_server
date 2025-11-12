package markit.community.community_post;

import markit.community.community_topic.CommunityTopic;
import markit.members.domain.Member;
import lombok.Data;

import java.util.List;

public class CommunityPostRequest {

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

    @Data
    public static class UpdateDTO {
        private String title;
        private String content;
        private String location;
        private Long topicId;
        private List<String> addImages;
        private List<String> deleteImages;
    }

    @Data
    public static class SearchDTO {
        private String keyword;
        private List<String> categories;
        private String sortType; // 인기순, 최신순, 조회순
    }
}

