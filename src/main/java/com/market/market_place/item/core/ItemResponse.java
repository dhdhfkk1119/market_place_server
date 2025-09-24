package com.market.market_place.item.core;

import com.market.market_place.item.item_category.ItemCategory;
import com.market.market_place.item.item_image.ItemImage;
import com.market.market_place.item.status.TradeStatus;
import io.grpc.LoadBalancer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;

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
        private LocationDTO tradeLocation;
        private String thumbnail;
        private Long viewCount;
        private Integer favoriteCount;
        private Long itemCategoryId;
        private List<String> tags;
        private TradeStatus status;

        public static ItemListDTO from(Item item) {
            GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);

            String thumbUrl = Optional.ofNullable(item.getImages())
                    .orElseGet(Collections::emptyList)
                    .stream()
                    .map(ItemImage::getImageUrl)
                    .findFirst()
                    .orElse("/static/img/placeholder.png");

            String categoryName = Optional.ofNullable(item.getItemCategory())
                    .map(ItemCategory::getName)
                    .orElse("기타");
            LocationDTO location = Optional.ofNullable(item.getTradeLocation())
                    .map(LocationDTO::new)
                    .orElse(null);

            int favCount = Optional.ofNullable(item.getFavorites())
                    .map(List::size)
                    .orElse(0);

            return ItemListDTO.builder()
                    .id(item.getId())
                    .title(item.getTitle())
                    .content(item.getContent())
                    .price(item.getPrice())
                    .itemCategoryName(categoryName)
                    .tradeLocation(location)
                    .thumbnail(thumbUrl)
                    .favoriteCount(favCount)
                    .viewCount(item.getViewCount() == null ? 0L : item.getViewCount())
                    .itemCategoryId(item.getItemCategory().getId())
                    .tags(item.getItemTags() == null ? List.of() :
                            item.getItemTags().stream()
                                    .map(itemTag -> itemTag.getTag().getDisplayName())
                                    .distinct()
                                    .toList())
                    .status(item.getStatus())
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
        private LocationDTO tradeLocation;
        private List<String> base64Images;
        private Integer favoriteCount;
        private String sellerProfileUrl;
        private String sellerAddress;
        private Double retransactionRate;
        private Long viewCount;
        private List<String> tags;
        private boolean liked;
        private TradeStatus status;

        public static ItemDetailDTO from(Item item, boolean liked) {
            LocationDTO location = Optional.ofNullable(item.getTradeLocation())
                    .map(LocationDTO::new)
                    .orElse(null);

            return ItemDetailDTO.builder()
                    .id(item.getId())
                    .itemCategoryId(item.getItemCategory().getId())
                    .sellerId(item.getMember().getId())
                    .title(item.getTitle())
                    .content(item.getContent())
                    .price(item.getPrice())
                    .sellerName(item.getMember().getMemberProfile().getName())
                    .tradeLocation(location)
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
                    .tags(item.getItemTags() == null ? List.of() :
                            item.getItemTags().stream()
                                    .map(itemTag -> itemTag.getTag().getDisplayName())
                                    .distinct()
                                    .toList())
                    .liked(liked)
                    .status(item.getStatus())
                    .build();
        }
    }

    @Data
    public static class ItemSaveDTO {
        private Long itemCategoryId;
        private LocationDTO tradeLocation;
        private String title;
        private String content;
        private Long price;

        @Builder
        public ItemSaveDTO(Item item) {
            this.content = item.getContent();
            this.itemCategoryId = item.getItemCategory().getId();
            this.tradeLocation = new LocationDTO(item.getTradeLocation());
            this.price = item.getPrice();
            this.title = item.getTitle();
        }
    }

    @Data
    public static class ItemUpdateDTO {
        private LocationDTO tradeLocation;
        private String title;
        private String content;
        private Long price;

        @Builder
        public ItemUpdateDTO(Item item) {
            this.content = item.getContent();
            this.tradeLocation = new LocationDTO(item.getTradeLocation());
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
        private String createdAt;
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
                    .createdAt(item.getTime())
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

    @Data
    public static class LocationDTO {
        private double latitude;  // 위도
        private double longitude; // 경도

        public LocationDTO(Point point) {
            if (point != null) {
                this.latitude = point.getY();  // Y좌표가 위도
                this.longitude = point.getX(); // X좌표가 경도
            }
        }
    }
}