package com.market.market_place.item.core;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemSearchRequest {
    private String keyword;
    private String sortBy;
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