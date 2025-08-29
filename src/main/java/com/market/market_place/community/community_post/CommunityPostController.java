package com.market.market_place.community.community_post;

import com.market.market_place._core._utils.ApiUtil;
import com.market.market_place._core._utils.JwtUtil;
import com.market.market_place._core.auth.Auth;
import com.market.market_place.members.domain.Member;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/community/posts")
@RequiredArgsConstructor
public class CommunityPostController {

    private final CommunityPostService postService;

    // 글 작성
    @Auth(roles = Member.MemberRole.USER)
    @PostMapping
    public ResponseEntity<?> save(@Valid @RequestBody CommunityPostRequest.SaveDTO saveDTO,
                                  @RequestAttribute("sessionUser")JwtUtil.SessionUser sessionUser){

        CommunityPostResponse.ResponseDTO savedPost = postService.save(saveDTO, sessionUser);
        return ResponseEntity.ok(ApiUtil.success(savedPost));
    }

}
