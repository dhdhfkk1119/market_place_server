package markit.item.item_report.dto;

import markit.item.item_report._enum.ItemReportStatus;
import markit.item.item_report._enum.ProcessResult;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class ItemReportProcessResponse {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ItemReportProcessDetailDTO {
        private Long processId;
        private Long reportId;
        private Long itemId;
        private Long adminId;
        private String reason;
        private ProcessResult result;
        private LocalDateTime processedAt;
        private ItemReportStatus finalStatus;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ItemReportProcessListDTO {
        private Long processId;
        private Long reportId;
        private Long itemId;
        private Long adminId;
        private String reason;
        private ProcessResult result;
        private LocalDateTime processedAt;
    }


}
