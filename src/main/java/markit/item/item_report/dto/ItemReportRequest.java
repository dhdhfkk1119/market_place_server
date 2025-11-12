package markit.item.item_report.dto;

import markit.item.core.Item;
import markit.item.item_report.entity.ItemReport;
import markit.members.domain.Member;
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
