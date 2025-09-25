package com.market.market_place.item.praise;

public enum PraiseMessage {

    DEFAULT("review.default"),
    SEPARATOR("review.separator"),
    PREFIX("review.prefix");

    private final String key;

    PraiseMessage(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
