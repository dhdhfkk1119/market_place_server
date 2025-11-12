package markit.community.community_report_process;

import markit.community.community_report.CommunityReport;
import markit.community.community_report.CommunityReportStatus;
import markit.members.domain.Member;
import lombok.Data;

public class CommunityReportProcessRequest {

    @Data
    public static class RequestDTO{
        private CommunityReportStatus status;
        private String adminComment;

       public CommunityReportProcess toEntity(CommunityReport report, Member admin){
           return CommunityReportProcess.builder()
                   .report(report)
                   .admin(admin)
                   .status(this.status)
                   .adminComment(this.adminComment)
                   .build();
       }
    }
}
