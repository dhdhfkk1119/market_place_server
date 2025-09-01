package com.market.market_place.item.core;

import com.market.market_place._core._utils.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RequiredArgsConstructor
@RestController
public class ItemController {

    private final ItemService itemService;

    @PostMapping("/api/items")
    public ResponseEntity<?> save(
            @RequestAttribute("sessionUser") JwtUtil.SessionUser sessionUser,
            @RequestBody ItemRequest.SaveDTO dto
    ) {
        ItemResponse.ItemSaveDTO body = itemService.save(sessionUser.getId(),dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }


}
