package markit.item.core;

import markit.item.item_category.ItemCategory;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;

import java.util.List;

public class ItemRequest {

    @Data
    public static class ItemSaveDTO {

        @NotBlank
        private Long itemCategoryId;
        private String title;
        private String content;
        private Long price;
        private List<String> base64Images;
        private List<String> tags;
        private Point tradeLocation;

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

        private Long itemCategoryId;
        private String title;
        private String content;
        private Long price;
        private Point tradeLocation;

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
        private Point tradeLocation;
        private Double distanceInMeter;

        @Builder.Default
        private int page = 0;

        @Builder.Default
        private int size = 10;

        public String getSortByProp() {
            if (sortBy == null) return "createdAt";
            return switch (sortBy.toLowerCase()) {
                case "popular" -> "averageRating";
                case "price"   -> "price";
                default        -> "createdAt";
            };
        }
    }

    @Data
    public static class SearchByLocationDTO {
        private double lat;
        private double lng;
        private int radius;
    }
}