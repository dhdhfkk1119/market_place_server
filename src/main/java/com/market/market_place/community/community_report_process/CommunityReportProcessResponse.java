package com.market.market_place.community.community_report_process;

import com.market.market_place.community.community_report.CommunityReport;
import com.market.market_place.community.community_report.CommunityReportStatus;
import lombok.Builder;
import lombok.Data;

import java.util.List;

public class CommunityReportProcessResponse {

    @Data
    public static class ListDTO{
        private Long processId;
        private Long reportId;
        private String postTitle;
        private CommunityReportStatus status;
        private String createdAt;

        @Builder
        public ListDTO(CommunityReportProcess process) {
            this.processId = process.getId();
            this.reportId = process.getReport().getId();
            this.postTitle = process.getReport().getPost().getTitle();
            this.status = process.getStatus();
            this.createdAt = process.getTime();
        }
    }

    @Data
    public static class DetailDTO{
        private Long reportId;
        private Long postId;
        private String postTitle;
        private String postContent;
        private String reporterName;
        private String reason;
        private CommunityReportStatus status;

        @Builder
        public DetailDTO(CommunityReport report) {
            this.reportId = report.getId();
            this.postId = report.getPost().getId();
            this.postTitle = report.getPost().getTitle();
            this.postContent = report.getPost().getContent();
            this.reporterName = report.getReporter().getMemberProfile().getName();
            this.reason = report.getReason();
            this.status = report.getStatus();
        }
    }
}
