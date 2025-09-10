package com.market.market_place.community.community_report;

import com.market.market_place.community.community_report_process.CommunityReportProcess;
import lombok.Builder;
import lombok.Data;

import java.util.List;

public class CommunityReportResponse {

    @Data
    public static class CreateDTO {
        private Long id;
        private String message;
        private Long postId;
        private Long reporterId;
        private String reason;
        private CommunityReportStatus status;
        private String createdAt;

        @Builder
        public CreateDTO(CommunityReport report, String message) {
            this.id = report.getId();
            this.message = message;
            this.postId = report.getPost().getId();
            this.reporterId = report.getReporter().getId();
            this.reason = report.getReason();
            this.status = report.getStatus();
            this.createdAt = report.getTime();
        }
    }

    // 전체조회
    @Data
    public static class ListDTO{
        private Long id;
        private Long postId;
        private String reason;
        private CommunityReportStatus status;
        private String createdAt;

        public ListDTO(CommunityReport report) {
            this.id = report.getId();
            this.postId = report.getPost().getId();
            this.reason = report.getReason();
            this.status = report.getStatus();
            this.createdAt = report.getTime();
        }
    }

    // 상세 조회
    @Data
    public static class DetailDTO{
        private Long id;
        private Long postId;
        private String postTitle;
        private String postContent;
        private String reason;
        private CommunityReportStatus status;
        private String createdAt;
        private List<CommunityReportProcess> adminComments;

        public DetailDTO(CommunityReport report) {
            this.id = report.getId();
            this.postId = report.getPost().getId();
            this.postTitle = report.getPost().getTitle();
            this.postContent = report.getPost().getContent();
            this.reason = report.getReason();
            this.status = report.getStatus();
            this.createdAt = report.getTime();
            this.adminComments = report.getAdminComments();
        }
    }
}
