package markit.item.item_report.support;

import markit.item.item_report._enum.ItemReportStatus;
import markit.item.item_report._enum.ProcessResult;

public final class ItemReportPolicy {

    private ItemReportPolicy() {
    }


    public static boolean canTransition(ItemReportStatus current, ProcessResult result) {
        return switch (current) {
            case PENDING, IN_PROGRESS -> true;
            case RESOLVED, BAD_RESOLVED -> false;
        };
    }

    public static ItemReportStatus toStatus(ProcessResult result) {
        return switch (result) {
            case ACCEPTED -> ItemReportStatus.BAD_RESOLVED; // 제재 수락
            case REJECTED -> ItemReportStatus.RESOLVED; // 기각(정상 종결)
            // 필요 시 다른 값 추가 (예: CANCELED 등)
            default -> ItemReportStatus.RESOLVED;
        };
    }

    public static boolean isOpen(ItemReportStatus status) {
        return status == ItemReportStatus.PENDING || status == ItemReportStatus.IN_PROGRESS;
    }
}
