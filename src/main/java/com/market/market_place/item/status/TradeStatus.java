package com.market.market_place.item.status;

public enum TradeStatus {
    ON_SALE("판매중"),
    PENDING("예약중"),
    SOLD("거래완료");

    private final String statusText;

    TradeStatus(String statusText) {
        this.statusText = statusText;
    }

    public String getStatusText() {
        return statusText;
    }
}