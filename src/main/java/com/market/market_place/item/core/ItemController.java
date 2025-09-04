package com.market.market_place.item.core;

import com.market.market_place._core._utils.JwtUtil;
import com.market.market_place._core.auth.Auth;
import com.market.market_place.members.domain.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/items")
public class ItemController {

    private final ItemService itemService;

    @GetMapping("/{id}")
    public ResponseEntity<ItemResponse.ItemDetailDTO> detail(@PathVariable Long id) {


        ItemResponse.ItemDetailDTO item = itemService.findById(id);

        return ResponseEntity.ok(item);
    }

    @GetMapping("/")
    public ResponseEntity<Page<ItemResponse.ItemListDTO>> list(
            @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC)
            Pageable pageable
    ) {
        Page<ItemResponse.ItemListDTO> body = itemService.findAll(pageable);
        return ResponseEntity.ok(body);
    }

    @Auth(roles = {Role.ADMIN, Role.USER})
    @PostMapping
    public ResponseEntity<?> save(
            @RequestAttribute("sessionUser") JwtUtil.SessionUser sessionUser,
            @RequestBody ItemRequest.ItemSaveDTO dto
    ) {
        ItemResponse.ItemSaveDTO body = itemService.save(sessionUser.getId(), dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    @Auth(roles = {Role.ADMIN, Role.USER})
    @PatchMapping("/{id}")
    public ResponseEntity<ItemResponse.ItemUpdateDTO> update(
            @PathVariable Long id,
            @RequestAttribute("sessionUser") JwtUtil.SessionUser sessionUser,
            @RequestBody ItemRequest.ItemUpdateDTO dto
    ) {
        ItemResponse.ItemUpdateDTO body =
                itemService.update(id, sessionUser.getId(), dto);
        return ResponseEntity.ok(body);
    }

    @Auth(roles = {Role.ADMIN, Role.USER})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            @RequestAttribute("sessionUser") JwtUtil.SessionUser sessionUser
    ) {
        itemService.delete(id, sessionUser.getId());
        return ResponseEntity.noContent().build();
    }
}
