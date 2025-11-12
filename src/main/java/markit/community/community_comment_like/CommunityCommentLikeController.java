package markit.community.community_comment_like;

import markit._core._utils.ApiUtil;
import markit._core._utils.JwtUtil;
import markit._core.auth.Auth;
import markit.members.domain.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/community/comments")
@RequiredArgsConstructor
public class CommunityCommentLikeController {

    private final CommunityCommentLikeService commentLikeService;

    @Auth(roles = Role.USER)
    @PostMapping("/{commentId}/like")
    public ResponseEntity<?> toggleLike(@PathVariable Long commentId,
                                        @RequestAttribute("sessionUser")JwtUtil.SessionUser sessionUser){
        CommunityCommentLikeResponse.ResponseDTO likeResponse = commentLikeService.toggleLike(commentId, sessionUser);
        return ResponseEntity.ok(ApiUtil.success(likeResponse));
    }

    @Auth(roles = Role.USER)
    @GetMapping("/{commentId}/like/count")
    public ResponseEntity<?> getLikeCount(@PathVariable Long commentId){
        Long count = commentLikeService.getLikeCount(commentId);
        return ResponseEntity.ok(ApiUtil.success(count));
    }
}
