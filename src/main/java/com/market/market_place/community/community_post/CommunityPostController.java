package com.market.market_place.community.community_post;

import com.market.market_place._core._utils.ApiUtil;
import com.market.market_place._core._utils.JwtUtil;
import com.market.market_place._core.auth.Auth;
import com.market.market_place.members.domain.Role;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/community/posts")
@RequiredArgsConstructor
public class CommunityPostController {

    private final CommunityPostService postService;

    // 전체조회
    @GetMapping
    public ResponseEntity<ApiUtil.ApiResult<List<CommunityPostResponse.ListDTO>>> list(
            @PageableDefault(size = 5, sort = "id", direction = Sort.Direction.DESC) Pageable pageable){
        List<CommunityPostResponse.ListDTO> posts = postService.findAllPosts(pageable);
        return ResponseEntity.ok(ApiUtil.success(posts));
    }

    // 상세조회
    @GetMapping("/{id}")
    public ResponseEntity<ApiUtil.ApiResult<CommunityPostResponse.DetailDTO>> detail(
            @PathVariable Long id){

        CommunityPostResponse.DetailDTO detailDTO = postService.detail(id);
        return ResponseEntity.ok(ApiUtil.success(detailDTO));
    }

    // 글 작성
    @Auth(roles = {Role.USER, Role.ADMIN})
    @PostMapping
    public ResponseEntity<?> save(@Valid @RequestBody CommunityPostRequest.SaveDTO saveDTO,
                                  @RequestAttribute("sessionUser")JwtUtil.SessionUser sessionUser){

        CommunityPostResponse.ResponseDTO savedPost = postService.save(saveDTO, sessionUser);
        return ResponseEntity.ok(ApiUtil.success(savedPost));
    }

    // 수정
    @Auth(roles = {Role.USER, Role.ADMIN})
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id,
                                    @Valid @RequestBody CommunityPostRequest.UpdateDTO updateDTO,
                                    @RequestAttribute("sessionUser") JwtUtil.SessionUser sessionUser){

        CommunityPostResponse.ResponseDTO updatePost = postService.update(id, updateDTO, sessionUser);
        return ResponseEntity.ok(ApiUtil.success(updatePost));
    }

    // 삭제
    @Auth(roles = {Role.USER, Role.ADMIN})
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id,
                                                  @RequestAttribute("sessionUser") JwtUtil.SessionUser sessionUser){
        postService.delete(id, sessionUser);
        return ResponseEntity.ok(ApiUtil.success("삭제 성공"));
    }
}
