package com.market.market_place.item.item_favorite;

import com.market.market_place._core._utils.JwtUtil;
import com.market.market_place._core.auth.Auth;
import com.market.market_place.members.domain.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/items")
@RequiredArgsConstructor
public class ItemFavoriteController {

    private final ItemFavoriteService itemFavoriteService;

    @Auth(roles = {Role.ADMIN, Role.USER})
    @PostMapping("/{itemId}/favorite")
    public ResponseEntity<ItemFavoriteResponse> toggleFavorite(
            @PathVariable Long itemId,
            @RequestAttribute("sessionUser") JwtUtil.SessionUser sessionUser
    ) {
        ItemFavoriteResponse body = itemFavoriteService.toggleFavorite(itemId, sessionUser.getId());
        return ResponseEntity.ok(body);
    }

    @Auth(roles = {Role.ADMIN, Role.USER})
    @GetMapping("/{itemId}/favorite")
    public ResponseEntity<ItemFavoriteResponse> getFavoriteStatus(
            @PathVariable Long itemId,
            @RequestAttribute("sessionUser") JwtUtil.SessionUser sessionUser
    ) {
        ItemFavoriteResponse body = itemFavoriteService.getFavoriteStatus(itemId,sessionUser.getId());
        return ResponseEntity.ok(body);
    }
}
