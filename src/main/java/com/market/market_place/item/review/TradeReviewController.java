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
API 명세 (클라이언트 참고)

[공통]
- 인증: Authorization 헤더에 'Bearer {accessToken}' 전달 (JwtUtil 참고)
- 응답 래핑: ApiUtil.ApiResult { success, response, error }

1) 리뷰 작성
- Method: POST
- URL: /api/v1/trade-reviews
- Auth: USER, ADMIN
- Body(JSON): { "tradeId": number, "content": string(<=100), "rating": number(1~5) }
- Response: { "success": true, "response": TradeReviewResponse, "error": null }

2) 리뷰 수정
- Method: PUT
- URL: /api/v1/trade-reviews/{reviewId}
- Auth: USER, ADMIN (본인 리뷰만)
- Body(JSON): { "tradeId": number, "content": string(<=100), "rating": number(1~5) }
- Response: { "success": true, "response": TradeReviewResponse, "error": null }

3) 리뷰 삭제
- Method: DELETE
- URL: /api/v1/trade-reviews/{reviewId}
- Auth: USER, ADMIN (본인 리뷰만)
- Response: { "success": true, "response": "삭제 성공", "error": null }

4) 리뷰 단일 조회(본인)
- Method: GET
- URL: /api/v1/trade-reviews/{reviewId}
- Auth: USER, ADMIN (본인 리뷰만)
- Response: { "success": true, "response": TradeReviewResponse, "error": null }
*/
}
