package com.market.market_place.item.status;

public enum ItemStatus {
    ON_SALE("판매중"),
    RESERVED("예약중"),
    SOLD("거래완료");

    private final String statusText;

    ItemStatus(String statusText) {
        this.statusText = statusText;
    }

    public String getStatusText() {
        return statusText;
    }
}