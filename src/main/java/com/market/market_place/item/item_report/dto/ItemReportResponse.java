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

        @Builder
        public ItemReportSaveDTO(ItemReport itemReport) {
            this.reason = itemReport.getReason();
        }
    }

    @Data
    @Builder
    public static class ItemReportDetailDTO {
        private Long id;
        private String reporterName;
        private Long itemId;
        private String reason;
        private ItemReportStatus status;
        private String createdAt;
        private String itemThumbnail;

        public static ItemReportDetailDTO from(ItemReport itemReport) {
            return ItemReportDetailDTO.builder()
                    .id(itemReport.getId())
                    .reporterName(itemReport.getReporter().getMemberProfile().getName())
                    .itemId(itemReport.getItem().getId())
                    .reason(itemReport.getReason())
                    .status(itemReport.getStatus())
                    .createdAt(itemReport.getTime())
                    .itemThumbnail(itemReport.getItem().getThumbnailUrl())
                    .build();
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ItemReportListDTO {
        private Long id;
        private Long itemId;
        private String reason;
        private ItemReportStatus status;
        private String createdAt;

        private String itemThumbnail;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ItemReportResultDTO {
        private Long id;
        private String adminReason;
        private ProcessResult result;
        private LocalDateTime processDate;
    }
}
