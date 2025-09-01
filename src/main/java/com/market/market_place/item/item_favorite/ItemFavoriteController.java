package com.market.market_place.item.item_favorite;

import com.market.market_place._core._utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/items")
@RequiredArgsConstructor
public class ItemFavoriteController {

    private final ItemFavoriteService itemFavoriteService;


    @PostMapping("/{id}/favorite")
    public ResponseEntity<ItemFavoriteResponse> toggleFavorite(
            @PathVariable Long itemId,
            @RequestAttribute("sessionUser") JwtUtil.SessionUser sessionUser
    ) {
        ItemFavoriteResponse body = itemFavoriteService.toggleFavorite(itemId, sessionUser.getId());
        return ResponseEntity.ok(body);
    }

    @GetMapping("/{id}/favorite")
    public ResponseEntity<ItemFavoriteResponse> getFavoriteStatus(
            @PathVariable Long itemId,
            @RequestAttribute("sessionUser") JwtUtil.SessionUser sessionUser
    ) {
        ItemFavoriteResponse body = itemFavoriteService.getFavoriteStatus(itemId,sessionUser.getId());
        return ResponseEntity.ok(body);
    }



}
