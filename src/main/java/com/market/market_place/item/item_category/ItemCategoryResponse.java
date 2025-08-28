package com.market.market_place.item.item_category;

import lombok.Builder;
import lombok.Data;

public class ItemCategoryResponse {

    @Data
    public static class ItemCategoryDetailDTO {
        private String title;

        @Builder
        public ItemCategoryDetailDTO(ItemCategory itemCategory) {
            this.title = itemCategory.getName();
        }
    }

    @Data
    @Builder
    public static class ItemCategoryListDTO {
        private String name;

        public static ItemCategoryListDTO fromEntity(ItemCategory itemCategory) {
            return ItemCategoryListDTO.builder()
                    .name(itemCategory.getName())
                    .build();
        }
    }

    @Data
    public static class ItemCategorySaveDTO {
        private String name;

        @Builder
        public ItemCategorySaveDTO(ItemCategory itemCategory) {
            this.name = itemCategory.getName();
        }
    }

    @Data
    public static class ItemCategoryUpdateDTO {
        private String name;

        @Builder
        public ItemCategoryUpdateDTO(ItemCategory itemCategory) {
            this.name = itemCategory.getName();
        }
    }

}
