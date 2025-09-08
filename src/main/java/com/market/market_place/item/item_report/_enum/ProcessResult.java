package com.market.market_place.item.item_report._enum;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ProcessResult {
    ACCEPTED("승인"),
    REJECTED("반려");

    private final String label;

    ProcessResult(String label) {
        this.label = label;
    }

    @JsonValue
    public String getLabel() {
        return label;
    }

    public String getCode() {
        return name();
    }

}
