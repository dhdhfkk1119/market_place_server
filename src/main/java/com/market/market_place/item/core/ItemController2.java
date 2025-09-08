package com.market.market_place.item.core;

import com.market.market_place._core._utils.ApiUtil;
import com.market.market_place._core._utils.JwtUtil;
import com.market.market_place._core.auth.Auth;
import com.market.market_place.members.domain.Role;
import com.market.market_place.members.dto_profile.MyInfoResponse;
import com.market.market_place.members.services.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/item2")
@RequiredArgsConstructor
public class ItemController2 {

    private final ItemService2 itemService2;
    private final MemberService memberService;

    @Auth(roles = {Role.ADMIN, Role.USER})
    @GetMapping
    public ResponseEntity<Page<ItemResponse.ItemListDTO>> getItems(
            ItemSearchRequest searchRequest,
            @RequestAttribute("sessionUser") JwtUtil.SessionUser sessionUser) {

        Page<ItemResponse.ItemListDTO> items = itemService2.getItems(searchRequest, sessionUser);
        return ResponseEntity.ok(items);
    }
}