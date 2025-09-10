package com.market.market_place.item.item_report.dto;

import com.market.market_place.item.core.Item;
import com.market.market_place.item.item_report.entity.ItemReport;
import com.market.market_place.members.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class ItemReportRequest {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ItemReportSaveDTO {
        private String reason;

        public ItemReport toEntity(Item item, Member reporter) {
            return ItemReport.builder()
                    .item(item)
                    .reporter(reporter)
                    .reason(this.reason)
                    .build();
        }
    }
}
