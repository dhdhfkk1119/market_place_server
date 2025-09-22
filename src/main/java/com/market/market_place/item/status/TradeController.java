package com.market.market_place.item.status;

import com.market.market_place._core._utils.JwtUtil;
import com.market.market_place._core.auth.Auth;
import com.market.market_place.members.domain.Role;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/trade")
@RequiredArgsConstructor
public class TradeController {

    private final TradeService tradeService;

    @PostMapping
    public ResponseEntity<TradeResponse> createTrade(@RequestBody @Valid TradeRequest request) {
        TradeResponse response = tradeService.createTrade(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 구매내역
    @Auth(roles = {Role.ADMIN, Role.USER})
    @GetMapping("/purchases")
    public ResponseEntity<List<TradeResponse.MyTradeListItemDTO>> getPurchases(
            @RequestAttribute("sessionUser")JwtUtil.SessionUser sessionUser) {
        List<TradeResponse.MyTradeListItemDTO> purchases = tradeService.getMyPurchases(sessionUser.getId());
        return ResponseEntity.ok(purchases);
    }

}