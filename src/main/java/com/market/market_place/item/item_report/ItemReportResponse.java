package com.market.market_place.item.item_report;

import lombok.Data;

public class ItemReportResponse {

    @Data
    public static class ItemReportSaveDTO {
        private String reason;

        public ItemReportSaveDTO(ItemReport itemReport) {
            this.reason = itemReport.getReason();
        }
    }
}
