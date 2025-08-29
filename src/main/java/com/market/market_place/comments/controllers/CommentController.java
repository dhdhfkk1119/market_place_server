package com.market.market_place.comments.controllers;

import com.market.market_place._core._utils.ApiUtil;
import com.market.market_place._core._utils.JwtUtil;
import com.market.market_place._core.auth.Auth;
import com.market.market_place.comments.dto.CommentRequest;
import com.market.market_place.comments.dto.CommentResponse;
import com.market.market_place.comments.services.CommentService;
import com.market.market_place.members.domain.Member;
import com.market.market_place.members.services.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;
    private final MemberService memberService;

    @Auth(roles = {Member.MemberRole.USER, Member.MemberRole.ADMIN})
    @PostMapping("/notice/{noticeId}")
    public ResponseEntity<ApiUtil.ApiResult<CommentResponse>> createComment(
            @PathVariable Long noticeId,
            @RequestBody CommentRequest request,
            @RequestAttribute("sessionUser") JwtUtil.SessionUser sessionUser) {

        Member member = memberService.findMemberByLoginId(sessionUser.getLoginId());
        CommentResponse response = commentService.createComment(noticeId, request, member);
        return new ResponseEntity<>(ApiUtil.success(response), HttpStatus.CREATED);
    } // 유저와 어드민 게시기능

    @Auth(roles = {Member.MemberRole.USER, Member.MemberRole.ADMIN})
    @PutMapping("/{id}")
    public ResponseEntity<ApiUtil.ApiResult<CommentResponse>> updateComment(
            @PathVariable Long id,
            @RequestBody CommentRequest request,
            @RequestAttribute("sessionUser") JwtUtil.SessionUser sessionUser) {

        CommentResponse response = commentService.updateComment(id, request, sessionUser.getId(), sessionUser.getRole());
        return ResponseEntity.ok(ApiUtil.success(response));
    } // 유저와 어드민 삭제기능

    @Auth(roles = {Member.MemberRole.USER, Member.MemberRole.ADMIN})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long id,
            @RequestAttribute("sessionUser") JwtUtil.SessionUser sessionUser) {

        commentService.deleteComment(id, sessionUser.getId(), sessionUser.getRole());
        return ResponseEntity.noContent().build();
    } // 유저와 어드민 삭제기능

    @Auth(roles = {Member.MemberRole.USER, Member.MemberRole.ADMIN})
    @GetMapping("/notice/{noticeId}")
    public ResponseEntity<ApiUtil.ApiResult<List<CommentResponse>>> getCommentsByNoticeId(
            @PathVariable Long noticeId) {
        List<CommentResponse> response = commentService.getCommentsByNoticeId(noticeId);
        return ResponseEntity.ok(ApiUtil.success(response));
    } // 유저와 어드민 조회기능
}