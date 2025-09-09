package com.market.market_place.item.item_report.dto;

import com.market.market_place.item.item_report._enum.ItemReportStatus;
import com.market.market_place.item.item_report._enum.ProcessResult;
import com.market.market_place.item.item_report.entity.ItemReport;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class ItemReportResponse {

    @Data
    public static class ItemReportSaveDTO {
        private String reason;

        public ItemReportSaveDTO(ItemReport itemReport) {
            this.reason = itemReport.getReason();
        }

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ItemReportDetailDTO {

        private String reporterName;
        private Long itemId;
        private String reason;
        private ItemReportStatus status;
        private Timestamp createdAt;

        public ItemReportDetailDTO(ItemReport itemReport) {

            this.reporterName = itemReport.getReporter().getMemberProfile().getName();
            this.itemId = itemReport.getItem().getId();
            this.reason = itemReport.getReason();
            this.status = itemReport.getStatus();
            this.createdAt = itemReport.getCreatedAt();

        }

        public static ItemReportDetailDTO from(ItemReport itemReport) {
            return ItemReportDetailDTO.builder()
                    .reporterName(itemReport.getReporter().getMemberProfile().getName())
                    .itemId(itemReport.getItem().getId())
                    .reason(itemReport.getReason())
                    .status(itemReport.getStatus())
                    .createdAt(itemReport.getCreatedAt())
                    .build();
        }

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ItemReportListDTO {

        private Long itemId;
        private String reason;
        private ItemReportStatus status;
        private Timestamp createAt;

        public ItemReportListDTO(ItemReport itemReport) {
            this.itemId = itemReport.getItem().getId();
            this.reason = itemReport.getReason();
            this.status = itemReport.getStatus();
            this.createAt = itemReport.getCreatedAt();
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ItemReportResultDTO {
        private Long itemReportId;
        private String adminReason;
        private ProcessResult Result;
        private LocalDateTime processDate;
    }
}
