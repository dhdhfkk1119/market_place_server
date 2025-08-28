package com.market.market_place.item.core;

import lombok.Data;

public class ItemResponse {

    @Data
    public static class DetailDTO {

        private String title;
        private String content;
        private Integer price;

    }

}
