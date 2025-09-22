package com.market.market_place.item.item_favorite;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;

@Data
public class ItemFavoriteResponse {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StatusDTO {
        private Long itemId;
        private boolean liked;
        private Long favoriteCount;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FavoriteItemDTO {
        private Long itemId;
        private String title;
        private String thumbnailUrl;
        private Long price;
        private Point tradeLocation;
        private Long favoriteCount;
    }
}
