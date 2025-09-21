package com.market.market_place.item.core;

import com.market.market_place.item.item_category.ItemCategory;
import com.market.market_place.item.item_image.ItemImage;
import com.market.market_place.item.status.TradeStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


public class ItemResponse {

    @Data
    @Builder
    public static class ItemListDTO {
        private Long id;
        private String title;
        private String content;
        private Long price;

        private String itemCategoryName;
        private String tradeLocation;
        private String thumbnail;
        private Long viewCount;
        private Integer favoriteCount;
        private Long itemCategoryId;

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
                    .id(item.getId())
                    .title(item.getTitle())
                    .content(item.getContent())
                    .price(item.getPrice())
                    .itemCategoryName(categoryName)
                    .tradeLocation(town)
                    .thumbnail(thumbUrl)
                    .favoriteCount(favCount)
                    .viewCount(item.getViewCount() == null ? 0L : item.getViewCount())
                    .itemCategoryId(item.getItemCategory().getId())
                    .build();
        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ItemDetailDTO {
        private Long id;
        private Long itemCategoryId;
        private Long sellerId;
        private String title;
        private String content;
        private Long price;
        private String sellerName;
        private String tradeLocation;
        private List<String> base64Images;
        private Integer favoriteCount;
        private String sellerProfileUrl;
        private String sellerAddress;
        private Double retransactionRate;
        private Long viewCount;
        private boolean liked;

        public static ItemDetailDTO from(Item item,boolean liked) {
            return ItemDetailDTO.builder()
                    .id(item.getId())
                    .itemCategoryId(item.getItemCategory().getId())
                    .sellerId(item.getMember().getId())
                    .title(item.getTitle())
                    .content(item.getContent())
                    .price(item.getPrice())
                    .sellerName(item.getMember().getMemberProfile().getName())
                    .tradeLocation(item.getTradeLocation())
                    .base64Images(
                            item.getImages().stream()
                                    .map(ItemImage::getImageUrl)
                                    .toList()
                    )
                    .favoriteCount(Optional.ofNullable(item.getFavorites())
                            .map(List::size)
                            .orElse(0))
                    .sellerId(item.getMember().getId())
                    .sellerName(item.getMember().getMemberProfile().getName())
                    .sellerProfileUrl(item.getMember().getMemberProfile().getProfileImageBase64())
                    .sellerAddress(item.getMember().getAddress())
                    .retransactionRate(item.getAverageRating())
                    .viewCount(item.getViewCount() == null ? 0L : item.getViewCount())
                    .liked(liked)
                    .build();
        }
    }

    @Data
    public static class ItemSaveDTO {
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

    @Data
    @Builder
    public static class MySalesListItemDTO {
        private Long id;
        private String title;
        private Long price;
        private String thumbnailUrl;
        private Timestamp createdAt;
        private String statusLabel;
        public TradeStatus status;

        public static MySalesListItemDTO from(Item item) {
            String thumbUrl = Optional.ofNullable(item.getImages())
                    .orElseGet(Collections::emptyList)
                    .stream()
                    .map(ItemImage::getImageUrl)
                    .findFirst()
                    .orElse(null);

            return MySalesListItemDTO.builder()
                    .id(item.getId())
                    .title(item.getTitle())
                    .price(item.getPrice())
                    .thumbnailUrl(thumbUrl)
                    .createdAt(item.getCreatedAt())
                    .status(item.getStatus())
                    .statusLabel(toStatusLabel(item.getStatus()))
                    .build();
        }

        private static String toStatusLabel(TradeStatus status) {
            return switch (status) {
                case ON_SALE -> "판매중";
                case PENDING -> "예약중";
                case SOLD -> "판매완료";
            };
        }
    }
}