package markit.community.community_post_like;

import markit._core._utils.ApiUtil;
import markit._core._utils.JwtUtil;
import markit._core.auth.Auth;
import markit.members.domain.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/community/posts")
@RequiredArgsConstructor
public class CommunityPostLikeController {

    private final CommunityPostLikeService postLikeService;

    @Auth(roles = Role.USER)
    @PostMapping("/{postId}/like")
    public ResponseEntity<?> toggleLike(@PathVariable Long postId,
                                        @RequestAttribute("sessionUser")JwtUtil.SessionUser sessionUser){
        CommunityPostLikeResponse.ResponseDTO likeResponse = postLikeService.toggleLike(postId, sessionUser);
        return ResponseEntity.ok(ApiUtil.success(likeResponse));
    }

    @Auth(roles = Role.USER)
    @GetMapping("/{postId}/like/count")
    public ResponseEntity<?> getLikeCount(@PathVariable Long postId) {
        Long count = postLikeService.getLikeCount(postId);
        return ResponseEntity.ok(ApiUtil.success(count));
    }
}
