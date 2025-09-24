package com.market.market_place.item.status;

import com.market.market_place._core._utils.JwtUtil;
import com.market.market_place._core.auth.Auth;
import com.market.market_place.members.domain.Role;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/trade")
@RequiredArgsConstructor
public class TradeController {

    private final TradeService tradeService;

    @Auth(roles = {Role.ADMIN,Role.USER})
    @PostMapping("/{itemId}")
    public ResponseEntity<TradeResponse> createTrade(@PathVariable("itemId") Long itemId,
                                                     @RequestAttribute("sessionUser") JwtUtil.SessionUser sessionUser) {
        TradeResponse response = tradeService.createTrade(itemId,sessionUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 구매내역
    @Auth(roles = {Role.ADMIN, Role.USER})
    @GetMapping("/purchases")
    public ResponseEntity<Page<TradeResponse.MyTradeListItemDTO>> getPurchases(
            @RequestAttribute("sessionUser") JwtUtil.SessionUser sessionUser,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<TradeResponse.MyTradeListItemDTO> purchases = tradeService.getMyPurchases(sessionUser.getId(), pageable);
        return ResponseEntity.ok(purchases);
    }

}