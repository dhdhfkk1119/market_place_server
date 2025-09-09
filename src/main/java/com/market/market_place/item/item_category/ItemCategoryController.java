package com.market.market_place.item.item_category;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
//@RequestMapping("/api/itemcategories")
public class ItemCategoryController {

    private final ItemCategoryService itemCategoryService;

    @GetMapping("/api/itemcategories")
    public ResponseEntity<?> index() {
        List<ItemCategoryResponse.ItemCategoryListDTO> categiryList = itemCategoryService.findAll();
        return ResponseEntity.ok(categiryList);
    }


    @PostMapping("/api/itemcategories")
    public ResponseEntity<?> save(@RequestBody ItemCategoryRequest.SaveDTO dto) {

        ItemCategoryResponse.ItemCategorySaveDTO saved = itemCategoryService.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

}
