package com.market.market_place.community.community_report_process;

import com.market.market_place.community.community_report.CommunityReport;
import com.market.market_place.community.community_report.CommunityReportStatus;
import com.market.market_place.members.domain.Member;
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
