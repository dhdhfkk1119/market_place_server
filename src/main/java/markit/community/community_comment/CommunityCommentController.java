package markit.community.community_comment;

import markit._core._utils.ApiUtil;
import markit._core._utils.JwtUtil;
import markit._core.auth.Auth;
import markit.members.domain.Role;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/community/comments")
@RequiredArgsConstructor
public class CommunityCommentController {

    private final CommunityCommentService commentService;

    @GetMapping("/posts/{postId}")
    public ResponseEntity<ApiUtil.ApiResult<List<CommunityCommentResponse.ResponseDTO>>> list(
            @PathVariable Long postId){
        return ResponseEntity.ok(ApiUtil.success(commentService.findWithPost(postId)));
    }


    @Auth(roles = {Role.USER, Role.ADMIN})
    @PostMapping("/posts/{postId}")
    public ResponseEntity<?> save(@PathVariable Long postId,
                                  @Valid @RequestBody CommunityCommentRequest.SaveDTO saveDTO,
                                  @RequestAttribute("sessionUser")JwtUtil.SessionUser sessionUser){

        CommunityCommentResponse.ResponseDTO savedComment = commentService.save(postId, saveDTO, sessionUser);
        return ResponseEntity.ok(ApiUtil.success(savedComment));
    }

    @Auth(roles = {Role.USER, Role.ADMIN})
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id,
                                    @Valid @RequestBody CommunityCommentRequest.UpdateDTO updateDTO,
                                    @RequestAttribute("sessionUser")JwtUtil.SessionUser sessionUser){

        CommunityCommentResponse.ResponseDTO updateComment = commentService.update(id, updateDTO, sessionUser);
        return ResponseEntity.ok(ApiUtil.success(updateComment));
    }

    @Auth(roles = {Role.USER, Role.ADMIN})
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id,
                                    @RequestAttribute("sessionUser")JwtUtil.SessionUser sessionUser){
        commentService.delete(id, sessionUser);
        return ResponseEntity.ok(ApiUtil.success("삭제 성공"));
    }
}
