package com.market.market_place.community.community_post_like;

import com.market.market_place._core._utils.ApiUtil;
import com.market.market_place._core._utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/community/posts")
@RequiredArgsConstructor
public class CommunityPostLikeController {

    private final CommunityPostLikeService postLikeService;

    // 좋아요 토글 (등록/삭제)
    @PostMapping("/{postId}/like")
    public ResponseEntity<?> toggleLike(@PathVariable Long postId,
                                                                @RequestAttribute("sessionUser")JwtUtil.SessionUser sessionUser){
        CommunityPostLikeResponse.ResponseDTO likeResponse = postLikeService.toggleLike(postId, sessionUser);
        return ResponseEntity.ok(ApiUtil.success(likeResponse));
    }

    // 좋아요 수 조회
    @GetMapping("/{postId}/like/count")
    public ResponseEntity<?> getLikeCount(@PathVariable Long postId) {
        Long count = postLikeService.getLikeCount(postId);
        return ResponseEntity.ok(ApiUtil.success(count));
    }
}
