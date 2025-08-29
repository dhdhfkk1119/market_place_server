package com.market.market_place.item.core;

import lombok.Data;

public class ItemResponse {

    @Data
    public static class DetailDTO {

        private Long itemCategoryId;
        private Long memberAddressId;
        private String title;
        private String content;
        private Long price;

        public class fromEntity{

        }

    }

}
