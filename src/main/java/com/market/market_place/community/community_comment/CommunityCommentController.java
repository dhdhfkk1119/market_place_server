package com.market.market_place.community.community_comment;

import com.market.market_place._core._utils.ApiUtil;
import com.market.market_place._core._utils.JwtUtil;
import com.market.market_place._core.auth.Auth;
import com.market.market_place.members.domain.Member;
import com.market.market_place.members.domain.Role;
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

    // 조회
    @GetMapping("/posts/{postId}")
    public ResponseEntity<ApiUtil.ApiResult<List<CommunityCommentResponse.ResponseDTO>>> list(
            @PathVariable Long postId){
        return ResponseEntity.ok(ApiUtil.success(commentService.findWithPost(postId)));
    }


    // 저장
    @Auth(roles = {Role.USER, Role.ADMIN})
    @PostMapping("/posts/{postId}")
    public ResponseEntity<?> save(@PathVariable Long postId,
                                  @Valid @RequestBody CommunityCommentRequest.SaveDTO saveDTO,
                                  @RequestAttribute("sessionUser")JwtUtil.SessionUser sessionUser){

        CommunityCommentResponse.ResponseDTO savedComment = commentService.save(postId, saveDTO, sessionUser);
        return ResponseEntity.ok(ApiUtil.success(savedComment));
    }

    // 수정
    @Auth(roles = {Role.USER, Role.ADMIN})
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id,
                                    @Valid @RequestBody CommunityCommentRequest.UpdateDTO updateDTO,
                                    @RequestAttribute("sessionUser")JwtUtil.SessionUser sessionUser){

        CommunityCommentResponse.ResponseDTO updateComment = commentService.update(id, updateDTO, sessionUser);
        return ResponseEntity.ok(ApiUtil.success(updateComment));
    }

    // 삭제
    @Auth(roles = {Role.USER, Role.ADMIN})
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id,
                                    @RequestAttribute("sessionUser")JwtUtil.SessionUser sessionUser){
        commentService.delete(id, sessionUser);
        return ResponseEntity.ok(ApiUtil.success("삭제 성공"));
    }
}
