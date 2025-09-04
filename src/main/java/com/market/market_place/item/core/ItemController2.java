package com.market.market_place.item.core;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/items")
@RequiredArgsConstructor
public class ItemController2 {

    private final ItemService2 itemService2;

    @GetMapping
    public ResponseEntity<List<Item>> getItemsByPriceRange(@RequestParam(required = false)String priceRange) {
        List<Item> items = itemService2.getFreeItems(priceRange);
        return ResponseEntity.ok(items);
    }
}
