package markit.moderation.sanction.community_sanction;

import markit.community.community_report.CommunityReportStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommunitySanctionRequest {
    private String reason;
    private CommunityReportStatus status;
}
