package markit.community.community_report;

import markit.community.community_post.CommunityPost;
import markit.members.domain.Member;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

public class CommunityReportRequest {

    @Data
    public static class CreateDTO {
        @NotBlank(message = "신고 사유는 필수입니다.")
        private String reason;

        public CommunityReport toEntity(CommunityPost post, Member reporter){
            return CommunityReport.builder()
                    .post(post)
                    .reason(reason)
                    .reporter(reporter)
                    .status(CommunityReportStatus.PENDING)
                    .build();
        }
    }
}
