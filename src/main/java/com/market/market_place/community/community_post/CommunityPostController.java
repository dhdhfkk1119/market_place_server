package com.market.market_place.community.community_post;

import com.market.market_place._core._utils.ApiUtil;
import com.market.market_place._core._utils.JwtUtil;
import com.market.market_place._core.auth.Auth;
import com.market.market_place.members.domain.Role;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/community/posts")
@RequiredArgsConstructor
@Slf4j
public class CommunityPostController {

    private final CommunityPostService postService;

    @GetMapping
    public ResponseEntity<ApiUtil.ApiResult<List<CommunityPostResponse.ListDTO>>> list(
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        List<CommunityPostResponse.ListDTO> posts = postService.findAllPosts(pageable);
        log.info("전체 조회에 접속 했습니다 posts = {}", posts);
        return ResponseEntity.ok(ApiUtil.success(posts));
    }

    @Auth(roles = {Role.ADMIN,Role.USER})
    @GetMapping("/{id}")
    public ResponseEntity<ApiUtil.ApiResult<CommunityPostResponse.DetailDTO>> detail(
            @PathVariable Long id,
            @RequestParam(defaultValue = "latest") String sortType,
            @RequestAttribute("sessionUser") JwtUtil.SessionUser sessionUser) {

        CommunityPostResponse.DetailDTO detailPosts = postService.detail(id, sortType,sessionUser);
        return ResponseEntity.ok(ApiUtil.success(detailPosts));
    }

    @Auth(roles = {Role.USER, Role.ADMIN})
    @PostMapping
    public ResponseEntity<?> save(@Valid @RequestBody CommunityPostRequest.SaveDTO saveDTO,
                                  @RequestAttribute("sessionUser") JwtUtil.SessionUser sessionUser) {

        CommunityPostResponse.ResponseDTO savedPost = postService.save(saveDTO, sessionUser);
        return ResponseEntity.ok(ApiUtil.success(savedPost));
    }

    @Auth(roles = {Role.USER, Role.ADMIN})
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id,
                                    @Valid @RequestBody CommunityPostRequest.UpdateDTO updateDTO,
                                    @RequestAttribute("sessionUser") JwtUtil.SessionUser sessionUser) {

        CommunityPostResponse.ResponseDTO updatePost = postService.update(id, updateDTO, sessionUser);
        return ResponseEntity.ok(ApiUtil.success(updatePost));
    }

    @Auth(roles = {Role.USER, Role.ADMIN})
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id,
                                    @RequestAttribute("sessionUser") JwtUtil.SessionUser sessionUser) {
        postService.delete(id, sessionUser);
        return ResponseEntity.ok(ApiUtil.success("삭제 성공"));
    }

    @Auth(roles = {Role.USER, Role.ADMIN})
    @GetMapping("/search")
    public ResponseEntity<ApiUtil.ApiResult<Page<CommunityPostResponse.ListDTO>>> searchPosts(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "categories", required = false) List<String> categories,
            @RequestParam(value = "sortType", required = false, defaultValue = "LATEST") String sortType,
            Pageable pageable,
            @RequestAttribute("sessionUser") JwtUtil.SessionUser sessionUser) {

        Page<CommunityPost> postsPage = postService.search(
                keyword,
                categories,
                sortType,
                pageable
        );

        Page<CommunityPostResponse.ListDTO> resultPage = postsPage.map(CommunityPostResponse.ListDTO::new);

        return ResponseEntity.ok(ApiUtil.success(resultPage));
    }


    // 내가 쓴 게시글 목록 조회
    @Auth(roles = {Role.USER, Role.ADMIN})
    @GetMapping("/mine")
    public ResponseEntity<Page<CommunityPostResponse.ListDTO>> myCommunityPosts(
            @RequestAttribute("sessionUser") JwtUtil.SessionUser sessionUser,
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<CommunityPostResponse.ListDTO> result = postService.getMyPosts(sessionUser.getId(), pageable);
        return ResponseEntity.ok(result);
    }
}
