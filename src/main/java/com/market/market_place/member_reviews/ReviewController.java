package com.market.market_place.member_reviews;

import com.market.market_place._core._utils.ApiUtil;
import com.market.market_place._core._utils.JwtUtil;
import com.market.market_place._core.auth.Auth;
import com.market.market_place.members.domain.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    /**
     * 특정 멤버에 대한 리뷰 리스트 조회 API
     * 조회한 회원이 작성한 리뷰가 있다면 가장 위에 표시
     * @param memberId 리뷰 대상 멤버의 PK
     * @param sessionUser 로그인한 회원 정보
     * @return 해당 멤버에게 남겨진 리뷰 리스트 (조회자가 작성한 리뷰가 맨 위)
     */
    @GetMapping("/members/{memberId}")
    public ResponseEntity<ApiUtil.ApiResult<List<ReviewResponse.ResponseDTO>>> list(@PathVariable Long memberId,
        @RequestAttribute(value = "sessionUser", required = false) JwtUtil.SessionUser sessionUser) {
        Long viewerId = (sessionUser != null) ? sessionUser.getId() : null;
        return ResponseEntity.ok(ApiUtil.success(reviewService.findByReviewedIdWithViewerFirst(memberId, viewerId)));
    }

    // 리뷰 작성 API (USER, ADMIN 권한 필요)
    @Auth(roles = {Role.USER, Role.ADMIN})
    @PostMapping("")
    public ResponseEntity<?> save(@RequestBody ReviewRequest.SaveDTO saveDTO,
                                  @RequestAttribute("sessionUser") JwtUtil.SessionUser sessionUser) {
        ReviewResponse.ResponseDTO savedReview = reviewService.save(saveDTO, sessionUser);
        return ResponseEntity.ok(ApiUtil.success(savedReview));
    }

    // 리뷰 수정 API (USER, ADMIN 권한 필요)
    @Auth(roles = {Role.USER, Role.ADMIN})
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id,
                                    @RequestBody ReviewRequest.UpdateDTO updateDTO,
                                    @RequestAttribute("sessionUser") JwtUtil.SessionUser sessionUser) {
        ReviewResponse.ResponseDTO updatedReview = reviewService.update(id, updateDTO, sessionUser);
        return ResponseEntity.ok(ApiUtil.success(updatedReview));
    }

    // 리뷰 삭제 API (USER, ADMIN 권한 필요)
    @Auth(roles = {Role.USER, Role.ADMIN})
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id,
                                    @RequestAttribute("sessionUser") JwtUtil.SessionUser sessionUser) {
        reviewService.delete(id, sessionUser);
        return ResponseEntity.ok(ApiUtil.success("삭제 성공"));
    }
}
