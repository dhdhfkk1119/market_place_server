package com.market.market_place.item.item_category;

import lombok.Data;

public class ItemCategoryRequest {

    @Data
    public static class SaveDTO {
        private String name;

        public ItemCategory toEntity() {
            return new ItemCategory(null, name);
        }
    }

    @Data
    public static class UpdateDTO {
        private String name;

    }

}
