package com.market.market_place.qna.controller;

import com.market.market_place._core._utils.ApiUtil;
import com.market.market_place._core._utils.JwtUtil;
import com.market.market_place._core.auth.Auth;
import com.market.market_place.members.domain.Member;
import com.market.market_place.members.domain.Role;
import com.market.market_place.members.services.MemberService;
import com.market.market_place.qna.dto.QnaRequest;
import com.market.market_place.qna.dto.QnaResponse;
import com.market.market_place.qna.services.QnaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/qna")
public class QnaController {

    private final QnaService qnaService;
    private final MemberService memberService;

    @Auth(roles = {Role.USER, Role.ADMIN})
    @PostMapping
    public ResponseEntity<ApiUtil.ApiResult<QnaResponse>> createQna (
            @RequestBody QnaRequest request,
            @RequestAttribute("sessionUser")JwtUtil.SessionUser sessionUser) {
        Member member = memberService.findMember(sessionUser.getId());
        QnaResponse qnaResponse = qnaService.createQna(request, member);
        return new ResponseEntity<>(ApiUtil.success(qnaResponse), HttpStatus.CREATED);
    }

    @Auth(roles = {Role.USER, Role.ADMIN})
    @GetMapping
    public ResponseEntity<ApiUtil.ApiResult<List<QnaResponse>>> getAllQna() {
        List<QnaResponse> response = qnaService.getAllQna();
        return ResponseEntity.ok(ApiUtil.success(response));
    }

    @Auth(roles = {Role.USER, Role.ADMIN})
    @PutMapping("/{id}")
    public ResponseEntity<ApiUtil.ApiResult<QnaResponse>> updateQna(
            @PathVariable Long id,
            @RequestBody QnaRequest request,
            @RequestAttribute("sessionUser") JwtUtil.SessionUser sessionUser) {

        QnaResponse response = qnaService.updateQna(id, request, sessionUser.getId(), sessionUser.getRole());
        return ResponseEntity.ok(ApiUtil.success(response));
    }

    @Auth(roles = {Role.USER, Role.ADMIN})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQna(
            @PathVariable Long id,
            @RequestAttribute("sessionUser") JwtUtil.SessionUser sessionUser) {

        qnaService.deleteQna(id, sessionUser.getId(), sessionUser.getRole());
        return ResponseEntity.noContent().build();
    }
}
