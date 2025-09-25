package com.market.market_place.item.status;

import lombok.Getter;

@Getter
public enum TradeStatus {
    ON_SALE("판매중"),
    PENDING("거래중"),
    SOLD("거래완료");

    private final String label;

    TradeStatus(String label) {
        this.label = label;
    }
}