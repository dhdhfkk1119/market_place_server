package com.market.market_place.item.item_report.dto;

import com.market.market_place.item.core.Item;
import com.market.market_place.item.item_report._enum.ItemReportStatus;
import com.market.market_place.item.item_report.entity.ItemReport;
import com.market.market_place.members.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

        private Member reporter;
        private Item item;
        private String reason;
        private ItemReportStatus status;


        @Builder
        public ItemReportDetailDTO(ItemReport itemReport) {
            this.reporter = itemReport.getReporter();
            this.item = itemReport.getItem();
            this.reason = itemReport.getReason();
            this.status = itemReport.getStatus();
        }
    }
}
