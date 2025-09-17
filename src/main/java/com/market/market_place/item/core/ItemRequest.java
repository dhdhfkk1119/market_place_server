package com.market.market_place.item.core;

import com.market.market_place.item.item_category.ItemCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

public class ItemRequest {

    @Data
    public static class ItemSaveDTO {

        private Long itemCategoryId;
        private Long memberAddressId;
        private String title;
        private String content;
        private Long price;
        private List<String> base64Images;

        private String tradeLocation;

        public Item toEntity(ItemCategory itemCategory) {
            return Item.builder()
                    .itemCategory(itemCategory)
                    .tradeLocation(this.tradeLocation)
                    .title(this.title)
                    .content(this.content)
                    .price(this.price)
                    .build();
        }
    }

    @Data
    public static class ItemUpdateDTO {

        private Long memberAddressId;
        private String title;
        private String content;
        private Long price;
        private String tradeLocation;

        private List<String> base64Images;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class SearchDTO {
        private String keyword;
        private String sortBy;
        private List<String> tags;
        private String sortOrder;
        private Long minPrice;
        private Long maxPrice;
        private String priceRange;
        private Long itemCategoryId;
        private String tradeLocation;

        @Builder.Default
        private int page = 0;

        @Builder.Default
        private int size = 10;
    }
}
