package com.market.market_place.Reply.controller;

import com.market.market_place.Reply.dto.ReplyRequest;
import com.market.market_place.Reply.dto.ReplyResponse;
import com.market.market_place.Reply.services.ReplyService;
import com.market.market_place._core._utils.ApiUtil;
import com.market.market_place._core._utils.JwtUtil;
import com.market.market_place._core.auth.Auth;
import com.market.market_place.members.domain.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/qna")
public class ReplyController {

    private final ReplyService replyService;;

    @Auth(roles = {Role.USER, Role.ADMIN})
    @PostMapping("/{qnaId}/replies")
    public ResponseEntity<ApiUtil.ApiResult<ReplyResponse>> createReply(
            @PathVariable Long qnaId,
            @RequestBody ReplyRequest request,
            @RequestAttribute("sessionUser") JwtUtil.SessionUser sessionUser) {
        ReplyResponse response = replyService.createReply(qnaId, sessionUser.getId(), request);
        return new ResponseEntity<>(ApiUtil.success(response), HttpStatus.CREATED);
    }

    @Auth(roles = {Role.USER, Role.ADMIN})
    @DeleteMapping("/replies/{replyId}")
    public ResponseEntity<Void> deleteReply(
            @PathVariable Long replyId,
            @RequestAttribute("sessionUser")JwtUtil.SessionUser sessionUser) {
        replyService.deleteReply(replyId,sessionUser.getId(), sessionUser.getRole());
        return ResponseEntity.noContent().build();
    }

    @Auth(roles = {Role.USER, Role.ADMIN})
    @PutMapping("/replies/{replyId}")
    public ResponseEntity<ApiUtil.ApiResult<ReplyResponse>> updateReply(
            @PathVariable Long replyId,
            @RequestBody ReplyRequest request,
            @RequestAttribute("sessionUser") JwtUtil.SessionUser sessionUser) {
        ReplyResponse response = replyService.updateReply(replyId, request, sessionUser.getId());
        return ResponseEntity.ok(ApiUtil.success(response));
    }

    @Auth(roles = {Role.USER, Role.ADMIN})
    @GetMapping("/replies/{replyId}")
    public ResponseEntity<ApiUtil.ApiResult<ReplyResponse>> getReplyById(@PathVariable Long replyId) {
        ReplyResponse response = replyService.getReplyById(replyId);
        return ResponseEntity.ok(ApiUtil.success(response));
    }

}
