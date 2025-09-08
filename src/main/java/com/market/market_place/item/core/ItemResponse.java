package com.market.market_place.item.core;

import com.market.market_place.item.item_category.ItemCategory;
import com.market.market_place.item.item_image.ItemImage;
import lombok.Builder;
import lombok.Data;

import java.util.Collections;
import java.util.List;
import java.util.Optional;


public class ItemResponse {

    @Data
    @Builder
    public static class ItemListDTO {
        private String title;
        private String content;
        private Long price;

        private String itemCategoryName;
        private String tradeLocation;
        private String thumbnail;

        // private String status;
        private Integer favoriteCount;

        public static ItemListDTO from(Item item) {

            String thumbUrl = Optional.ofNullable(item.getImages())
                    .orElseGet(Collections::emptyList)
                    .stream()
                    .map(ItemImage::getImageUrl)
                    .findFirst()
                    .orElse("/static/img/placeholder.png");

            String categoryName = Optional.ofNullable(item.getItemCategory())
                    .map(ItemCategory::getName)
                    .orElse("기타");
            String town = Optional.ofNullable(item.getTradeLocation())
                    .orElse("미지정");

            int favCount = Optional.ofNullable(item.getFavorites())
                    .map(List::size)
                    .orElse(0);

            return ItemListDTO.builder()
                    .title(item.getTitle())
                    .content(item.getContent())
                    .price(item.getPrice())
                    .itemCategoryName(categoryName)
                    .tradeLocation(town)
                    .thumbnail(thumbUrl)
                    .favoriteCount(favCount)
                    .build();
        }
    }

    @Data
    public static class ItemDetailDTO {
        //이미지 거래방식 추가 필요
        private Long itemCategoryId;
        private String title;
        private String content;
        private Long price;
        private String tradeLocation;

        @Builder
        public ItemDetailDTO(Item item) {
            this.content = item.getContent();
            this.itemCategoryId = item.getItemCategory().getId();
            this.tradeLocation = item.getTradeLocation();
            this.price = item.getPrice();
            this.title = item.getTitle();
        }
    }

    @Data
    public static class ItemSaveDTO {
        //이미지 거래방식 추가 필요
        private Long itemCategoryId;
        private String tradeLocation;
        private String title;
        private String content;
        private Long price;

        @Builder
        public ItemSaveDTO(Item item) {
            this.content = item.getContent();
            this.itemCategoryId = item.getItemCategory().getId();
            this.tradeLocation = item.getTradeLocation();
            this.price = item.getPrice();
            this.title = item.getTitle();
        }
    }

    @Data
    public static class ItemUpdateDTO {

        private String tradeLocation;
        private String title;
        private String content;
        private Long price;

        @Builder
        public ItemUpdateDTO(Item item) {
            this.content = item.getContent();
            this.tradeLocation = item.getTradeLocation();
            this.price = item.getPrice();
            this.title = item.getTitle();
        }
    }


}
