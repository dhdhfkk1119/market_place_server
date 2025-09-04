package com.market.market_place.community.community_comment_like;

import com.market.market_place._core._utils.ApiUtil;
import com.market.market_place._core._utils.JwtUtil;
import com.market.market_place._core.auth.Auth;
import com.market.market_place.members.domain.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/community/comments")
@RequiredArgsConstructor
public class CommunityCommentLikeController {

    private final CommunityCommentLikeService commentLikeService;

    // 좋아요 토글(등록/삭제)
    @Auth(roles = Role.USER)
    @PostMapping("/{commentId}/like")
    public ResponseEntity<?> toggleLike(@PathVariable Long commentId,
                                        @RequestAttribute("sessionUser")JwtUtil.SessionUser sessionUser){
        CommunityCommentLikeResponse.ResponseDTO likeResponse = commentLikeService.toggleLike(commentId, sessionUser);
        return ResponseEntity.ok(ApiUtil.success(likeResponse));
    }

    // 좋아요 수 조회
    @Auth(roles = Role.USER)
    @GetMapping("/{commentId}/like/count")
    public ResponseEntity<?> getLikeCount(@PathVariable Long commentId){
        Long count = commentLikeService.getLikeCount(commentId);
        return ResponseEntity.ok(ApiUtil.success(count));
    }
}
