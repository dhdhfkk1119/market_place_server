package markit.community.community_comment;

import lombok.Builder;
import lombok.Data;


public class CommunityCommentResponse {

    @Data
    public static class ResponseDTO{
        private Long id;
        private String content;
        private String writerName;
        private int likeCount;
        private String displayTime;
        private boolean isModified;

        @Builder
        public ResponseDTO(CommunityComment comment) {
            this.id = comment.getId();
            this.content = comment.getContent();
            this.writerName = comment.getMember().getMemberProfile().getName();
            this.likeCount = comment.getLikeCount();

            if (comment.isModified()) {
                this.displayTime = comment.getUpdateTime(); // 수정된 시간
                this.isModified = true;
            } else {
                this.displayTime = comment.getCreateTime(); // 생성된 시간
                this.isModified = false;
            }
        }
    }
}
