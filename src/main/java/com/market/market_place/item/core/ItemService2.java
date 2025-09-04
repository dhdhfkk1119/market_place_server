package com.market.market_place.item.core;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemService2 {

    private final ItemRepository2 itemRepository2;

    @Transactional(readOnly = true)
    public List<Item> getFreeItems(String priceRange) {
        if ("free".equals(priceRange)) {
            return itemRepository2.findByPrice(0L);
        } else if ("lessThan5k".equals(priceRange)) {
            return itemRepository2.findByPriceLessThanEqual(5000L);
        } else if ("lessThan10k".equals(priceRange)) {
            return itemRepository2.findByPriceLessThanEqual(10000L);
        } else if ("lessThan20k".equals(priceRange)) {
            return itemRepository2.findByPriceLessThanEqual(20000L);
        } else {
            // 모든 상품 조회
            return itemRepository2.findAll();
        }
    }


}
