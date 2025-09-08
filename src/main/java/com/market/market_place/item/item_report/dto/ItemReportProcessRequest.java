package com.market.market_place.item.item_report.dto;

import com.market.market_place.item.item_report._enum.ProcessResult;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


public class ItemReportProcessRequest {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ItemReportProcessRequestUpdateDTO {
        private String reason;
        private ProcessResult result;
    }
}
