package com.market.market_place.item.status;

import com.market.market_place._core._utils.ApiUtil;
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

/*
{
  "거래 생성": {
    "method": "POST",
    "url": "/api/v1/trades",
    "description": "특정 상품에 대한 거래를 생성합니다. 거래가 생성되면 해당 상품은 'SOLD' 상태로 변경됩니다.",
    "auth": "필수 (USER, ADMIN)",
    "body": {
      "itemId": "number (필수, 거래할 상품의 ID)"
    },
    "response": {
      "success": true,
      "data": {
        "id": "number (거래 ID)",
        "itemId": "number (상품 ID)",
        "sellerId": "number (판매자 ID)",
        "buyerId": "number (구매자 ID)",
        "buyerReviewed": "boolean",
        "sellerReviewed": "boolean"
      }
    }
  },
  "구매 내역 조회": {
    "method": "GET",
    "url": "/api/v1/trades/purchases",
    "description": "로그인한 사용자의 구매 내역 목록을 페이지 단위로 조회합니다.",
    "auth": "필수 (USER, ADMIN)",
    "queryParams": [
      {
        "name": "page",
        "type": "number",
        "description": "조회할 페이지 번호 (0부터 시작)",
        "default": "0"
      },
      {
        "name": "size",
        "type": "number",
        "description": "한 페이지에 표시할 항목 수",
        "default": "10"
      },
      {
        "name": "sort",
        "type": "string",
        "description": "정렬 기준. 예: 'createdAt,desc' (거래일 내림차순)",
        "default": "createdAt,desc"
      }
    ],
    "response": {
      "success": true,
      "data": {
        "content": [
          {
            "tradeId": "number (거래 ID)",
            "itemId": "number (상품 ID)",
            "title": "string (상품 제목)",
            "price": "number (거래 가격)",
            "thumbnailUrl": "string (상품 썸네일 이미지 URL)",
            "tradedAt": "string (거래 생성일, ISO 8601 형식)",
            "tradeStatus": "string (거래 상태 ENUM, 예: 'SOLD', 'PENDING')",
            "tradeStatusLabel": "string (거래 상태 라벨, 예: '거래완료', '거래중')",
            "sellerId": "number (판매자 ID)",
            "sellerName": "string (판매자 이름)",
            "isReviewed": "boolean (구매자의 리뷰 작성 여부)",
            "reviewId": "number (리뷰 ID, 리뷰 작성 시 존재)",
            "reviewContent": "string (리뷰 내용, 리뷰 작성 시 존재)",
            "reviewRating": "number (리뷰 평점, 리뷰 작성 시 존재)"
          }
        ],
        "pageable": {
          "sort": { "sorted": "boolean", "unsorted": "boolean", "empty": "boolean" },
          "pageNumber": "number (현재 페이지 번호)",
          "pageSize": "number (페이지 크기)",
          "offset": "number",
          "paged": "boolean",
          "unpaged": "boolean"
        },
        "totalPages": "number (전체 페이지 수)",
        "totalElements": "number (전체 항목 수)",
        "last": "boolean (마지막 페이지 여부)",
        "numberOfElements": "number (현재 페이지의 항목 수)",
        "size": "number (페이지 크기)",
        "number": "number (현재 페이지 번호, 0부터 시작)",
        "sort": { "sorted": "boolean", "unsorted": "boolean", "empty": "boolean" },
        "first": "boolean (첫 페이지 여부)",
        "empty": "boolean (현재 페이지가 비어있는지 여부)"
      }
    }
  }
}
*/
}