package com.market.market_place.item.item_category;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class ItemCategoryController {

    private final ItemCategoryService itemCategoryService;

    @GetMapping("/api/itemcategories")
    public ResponseEntity<?> index() {
        List<ItemCategoryResponse.ItemCategoryListDTO> categiryList = itemCategoryService.findAll();
        return ResponseEntity.ok(categiryList);
    }
}
