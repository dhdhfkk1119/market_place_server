package com.market.market_place.item.core;

import com.market.market_place.item.item_category.ItemCategory;
import com.market.market_place.members.domain.MemberAddress;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.PageRequest;

import java.util.List;


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
            return ItemListDTO.builder()
                    .content(item.getContent())
                    .title(item.getTitle())
                    .price(item.getPrice())
                    .tradeLocation(item.getTradeLocation())
                    .itemCategoryName(item.getItemCategory().getName())
                    .thumbnail(item.getImages().get(0).getImageUrl())
                    .build();
        }
    }

    @Data
    public static class ItemDetailDTO {
        //이미지 거래방식 추가 필요
        private Long itemCategoryId;
        private Long memberAddressId;
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
