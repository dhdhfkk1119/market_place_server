package markit.item.item_favorite;

import markit._core._utils.JwtUtil;
import markit._core.auth.Auth;
import markit.members.domain.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/items")
@RequiredArgsConstructor
public class ItemFavoriteController {

    private final ItemFavoriteService itemFavoriteService;

    @Auth(roles = {Role.ADMIN, Role.USER})
    @PostMapping("/{itemId}/favorite")
    public ResponseEntity<ItemFavoriteResponse.StatusDTO> toggleFavorite(
            @PathVariable Long itemId,
            @RequestAttribute("sessionUser") JwtUtil.SessionUser sessionUser
    ) {
        ItemFavoriteResponse.StatusDTO body =
                itemFavoriteService.toggleFavorite(itemId, sessionUser.getId());
        return ResponseEntity.ok(body);
    }

    @Auth(roles = {Role.ADMIN, Role.USER})
    @GetMapping("/{itemId}/favorite")
    public ResponseEntity<ItemFavoriteResponse.StatusDTO> getFavoriteStatus(
            @PathVariable Long itemId,
            @RequestAttribute("sessionUser") JwtUtil.SessionUser sessionUser
    ) {
        ItemFavoriteResponse.StatusDTO body = itemFavoriteService.getFavoriteStatus(itemId, sessionUser.getId());
        return ResponseEntity.ok(body);
    }

    // 내 좋아요 목록
    @Auth(roles = {Role.ADMIN, Role.USER})
    @GetMapping("/favorites/me")
    public ResponseEntity<Page<ItemFavoriteResponse.FavoriteItemDTO>> getMyFavorites(
            @RequestAttribute("sessionUser") JwtUtil.SessionUser sessionUser,
            @PageableDefault(size = 10,sort = "createdAt",direction = Sort.Direction.DESC)
            Pageable pageable
    ) {
        Page<ItemFavoriteResponse.FavoriteItemDTO> body =
                itemFavoriteService.getMyFavoriteItems(sessionUser.getId(),pageable);
        return ResponseEntity.ok(body);
    }
}
