package com.market.market_place.item.item_favorite;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemFavoriteResponse {
    private Long itemId;
    private boolean liked;
    private Long favoriteCount;
}
