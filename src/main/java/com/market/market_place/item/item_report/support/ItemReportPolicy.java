package com.market.market_place.item.item_report.support;

import com.market.market_place.item.item_report._enum.ItemReportStatus;
import com.market.market_place.item.item_report._enum.ProcessResult;

public final class ItemReportPolicy {

    private ItemReportPolicy() {
    }

    public static boolean canTransition(ItemReportStatus current, ProcessResult result) {
        return switch (current) {
            case PENDING, IN_PROGRESS -> true;
            case RESOLVED -> false;
        };
    }

    public static ItemReportStatus toStatus(ProcessResult result) {
        return ItemReportStatus.RESOLVED;
    }

    public static boolean isOpen(ItemReportStatus status) {
        return status == ItemReportStatus.PENDING || status == ItemReportStatus.IN_PROGRESS;
    }
}
