package com.market.market_place.item.review;

import com.market.market_place._core._utils.ApiUtil;
import com.market.market_place._core._utils.JwtUtil;
import com.market.market_place._core.auth.Auth;
import com.market.market_place.members.domain.Role;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "거래 리뷰 API", description = "거래 후기에 대한 CRUD를 제공합니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/trade-reviews")
public class TradeReviewController {

    private final TradeReviewService tradeReviewService;

    @Auth(roles = {Role.USER, Role.ADMIN})
    @Operation(summary = "거래 후기 작성", description = "거래에 대한 후기를 작성합니다.")
    @PostMapping
    // 거래 후기 작성
    public ResponseEntity<ApiUtil.ApiResult<TradeReviewResponse>> createReview(
            @Validated @RequestBody TradeReviewRequest reviewRequest,
            @RequestAttribute("sessionUser") JwtUtil.SessionUser sessionUser
    ) {
        TradeReviewResponse response = tradeReviewService.createReview(sessionUser.getId(), reviewRequest);
        return ResponseEntity.ok(ApiUtil.success(response));
    }

    @Auth(roles = {Role.USER, Role.ADMIN})
    @Operation(summary = "거래 리뷰 수정", description = "작성한 거래 후기를 수정합니다.")
    @PutMapping("/{reviewId}")
    // 거래 리뷰 수정
    public ResponseEntity<ApiUtil.ApiResult<TradeReviewResponse>> updateReview(
            @Parameter(description = "리뷰 ID") @PathVariable Long reviewId,
            @Validated @RequestBody TradeReviewRequest reviewRequest,
            @RequestAttribute("sessionUser") JwtUtil.SessionUser sessionUser
    ) {
        TradeReviewResponse updated = tradeReviewService.updateReview(reviewId, reviewRequest, sessionUser.getId());
        return ResponseEntity.ok(ApiUtil.success(updated));
    }

    @Auth(roles = {Role.USER, Role.ADMIN})
    @Operation(summary = "거래 리뷰 삭제", description = "작성한 거래 후기를 삭제합니다.")
    @DeleteMapping("/{reviewId}")
    // 거래 리뷰 삭제
    public ResponseEntity<ApiUtil.ApiResult<String>> deleteReview(
            @Parameter(description = "리뷰 ID") @PathVariable Long reviewId,
            @RequestAttribute("sessionUser") JwtUtil.SessionUser sessionUser
    ) {
        tradeReviewService.deleteReview(reviewId, sessionUser.getId());
        return ResponseEntity.ok(ApiUtil.success("삭제 성공"));
    }

    // 내가 쓴 단일 리뷰 조회
    @Auth(roles = {Role.USER, Role.ADMIN})
    @Operation(summary = "거래 리뷰 단일 조회", description = "특정 리뷰를 조회합니다. 본인이 작성한 리뷰만 조회 가능합니다.")
    @GetMapping("/{reviewId}")
    public ResponseEntity<ApiUtil.ApiResult<TradeReviewResponse>> getReview(
            @Parameter(description = "리뷰 ID") @PathVariable Long reviewId,
            @RequestAttribute("sessionUser") JwtUtil.SessionUser sessionUser
    ) {
        TradeReviewResponse review = tradeReviewService.getReviewById(reviewId, sessionUser.getId());
        return ResponseEntity.ok(ApiUtil.success(review));
    }

/**
API 명세

"리뷰 작성": {
  "method": "POST",
  "url": "/api/v1/trade-reviews",
  "description": "거래에 대한 후기를 작성합니다. 구매자와 판매자 모두 작성 가능하며, 한 거래에 대해 한 번만 작성할 수 있습니다.",
  "auth": "필수 (USER, ADMIN)",
  "body": {
    "tradeId": "number (필수)",
    "content": "string (필수, 최대 100자)",
    "rating": "number (필수, 1~5)"
  },
  "response": {
    "success": true,
    "data": {
      "id": "number (리뷰 ID)",
      "content": "string (리뷰 내용)",
      "shortContent": "string (20자 요약)",
      "rating": "number (평점)",
      "reviewerLoginId": "string (작성자 로그인 ID)",
      "createdAt": "string (ISO 8601 형식)"
    }
  }
},
"리뷰 수정": {
  "method": "PUT",
  "url": "/api/v1/trade-reviews/{reviewId}",
  "description": "본인이 작성한 거래 후기를 수정합니다. URL의 {reviewId}에 수정할 리뷰의 ID를 입력합니다.",
  "auth": "필수 (USER, ADMIN)",
  "body": {
    "tradeId": "number (필수)",
    "content": "string (필수, 최대 100자)",
    "rating": "number (필수, 1~5)"
  },
  "response": {
    "success": true,
    "data": {
      "id": "number (리뷰 ID)",
      "content": "string (수정된 리뷰 내용)",
      "shortContent": "string (20자 요약)",
      "rating": "number (수정된 평점)",
      "reviewerLoginId": "string (작성자 로그인 ID)",
      "createdAt": "string (ISO 8601 형식)"
    }
  }
},
"리뷰 삭제": {
  "method": "DELETE",
  "url": "/api/v1/trade-reviews/{reviewId}",
  "description": "본인이 작성한 거래 후기를 삭제합니다. URL의 {reviewId}에 삭제할 리뷰의 ID를 입력합니다.",
  "auth": "필수 (USER, ADMIN)",
  "response": {
    "success": true,
    "data": "삭제 성공"
  }
},
"리뷰 단일 조회": {
  "method": "GET",
  "url": "/api/v1/trade-reviews/{reviewId}",
  "description": "본인이 작성한 특정 리뷰를 조회합니다. URL의 {reviewId}에 조회할 리뷰의 ID를 입력합니다.",
  "auth": "필수 (USER, ADMIN)",
  "response": "리뷰 작성 응답의 'data'와 동일"
}

*/
}
