package markit.item.status;

import markit._core._utils.ApiUtil;
import markit._core._utils.JwtUtil;
import markit._core.auth.Auth;
import markit.members.domain.Role;
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
@RequestMapping("/api/v1/trades")
@RequiredArgsConstructor
public class TradeController {

    private final TradeService tradeService;

    // 거래 생성
    @Auth(roles = {Role.ADMIN, Role.USER})
    @PostMapping
    public ResponseEntity<ApiUtil.ApiResult<TradeResponse>> createTrade(@RequestBody @Valid TradeRequest.CreateDTO requestDTO,
                                                                        @RequestAttribute("sessionUser") JwtUtil.SessionUser sessionUser) {
        TradeResponse response = tradeService.createTrade(requestDTO.getItemId(), sessionUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiUtil.success(response));
    }

    // 구매내역
    @Auth(roles = {Role.ADMIN, Role.USER})
    @GetMapping("/purchases")
    public ResponseEntity<ApiUtil.ApiResult<Page<TradeResponse.PurchaseListItemDTO>>> getPurchases(
            @RequestAttribute("sessionUser") JwtUtil.SessionUser sessionUser,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<TradeResponse.PurchaseListItemDTO> purchases = tradeService.getMyPurchases(sessionUser.getId(), pageable);
        return ResponseEntity.ok(ApiUtil.success(purchases));
    }

    // 단건 구매 카드 조회 (특정 카드만 갱신)
    @Auth(roles = {Role.ADMIN, Role.USER})
    @GetMapping("/{tradeId}")
    public ResponseEntity<ApiUtil.ApiResult<TradeResponse.PurchaseListItemDTO>> getPurchaseCard(
            @PathVariable Long tradeId,
            @RequestAttribute("sessionUser") JwtUtil.SessionUser sessionUser) {
        TradeResponse.PurchaseListItemDTO dto = tradeService.getMyPurchaseCard(tradeId, sessionUser.getId());
        return ResponseEntity.ok(ApiUtil.success(dto));
    }

/*
API 명세 (클라이언트 참고)

[공통]
- 인증: Authorization 헤더에 'Bearer {accessToken}' 전달 (JwtUtil 참고)
- 응답 래핑: ApiUtil.ApiResult { success, response, error }

1) 거래 생성
- Method: POST
- URL: /api/v1/trades
- Auth: USER, ADMIN
- Body(JSON): { "itemId": number }
- Response: { "success": true, "response": { "id": number, "itemId": number, "sellerId": number, "buyerId": number, "buyerReviewed": boolean, "sellerReviewed": boolean }, "error": null }

2) 구매 내역 조회(페이지)
- Method: GET
- URL: /api/v1/trades/purchases?page={0}&size={10}&sort=createdAt,desc
- Auth: USER, ADMIN
- Response: { "success": true, "response": Page<PurchaseListItemDTO>, "error": null }
  - PurchaseListItemDTO: { tradeId, itemId, title, price, thumbnailUrl, tradedAt, tradeStatus, tradeStatusLabel, sellerId, sellerName, isReviewed, reviewId?, reviewContent?, reviewRating? }

3) 단건 구매 카드 조회(특정 카드 갱신)
- Method: GET
- URL: /api/v1/trades/{tradeId}
- Auth: USER, ADMIN (본인 구매건만 조회 가능)
- Response: { "success": true, "response": PurchaseListItemDTO, "error": null }
*/
}