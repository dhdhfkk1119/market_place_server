package com.market.market_place.members.controllers;

import com.market.market_place._core._utils.ApiUtil;
import com.market.market_place._core._utils.JwtUtil;
import com.market.market_place.members.domain.Member;
import com.market.market_place.members.dtos.*;
import com.market.market_place.members.services.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;
    private final JwtUtil jwtUtil;

    // 회원가입
    @PostMapping("/register")
    public ResponseEntity<ApiUtil.ApiResult<MemberRegisterResponse>> registerMember(@Valid @RequestBody MemberRegisterRequest request) {
        MemberRegisterResponse response = memberService.registerMember(request);
        return new ResponseEntity<>(ApiUtil.success(response), HttpStatus.CREATED);
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<ApiUtil.ApiResult<MemberLoginResponse>> login(@RequestBody MemberLoginRequest request) {
        Member member = memberService.login(request);
        String token = JwtUtil.createToken(member);
        MemberLoginResponse response = new MemberLoginResponse(member, token);
        return ResponseEntity.ok(ApiUtil.success(response));
    }

    // 회원 정보 수정
    @PatchMapping("/{id}")
    public ResponseEntity<ApiUtil.ApiResult<MemberUpdateResponse>> updateMember(@PathVariable Long id, @Valid @RequestBody MemberUpdateRequest request) {
        Member updatedMember = memberService.updateMember(id, request);
        MemberUpdateResponse response = new MemberUpdateResponse(updatedMember);
        return ResponseEntity.ok(ApiUtil.success(response));
    }

    // 회원 상세 조회 (loginId 기준)
    @GetMapping("/{loginId}")
    public ResponseEntity<ApiUtil.ApiResult<MemberRegisterResponse>> getMember(@PathVariable String loginId) {
        Member member = memberService.findMemberByLoginId(loginId);
        MemberRegisterResponse response = new MemberRegisterResponse(member);
        return ResponseEntity.ok(ApiUtil.success(response));
    }

    // 회원 전체 조회
    @GetMapping
    public ResponseEntity<ApiUtil.ApiResult<List<MemberRegisterResponse>>> getAllMembers() {
        List<Member> members = memberService.findAllMembers();
        List<MemberRegisterResponse> response = members.stream()
                .map(MemberRegisterResponse::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiUtil.success(response));
    }

    // 회원 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMember(@PathVariable Long id) {
        memberService.deleteMember(id);
        return ResponseEntity.noContent().build();
    }

    // 상태 확인용
    @GetMapping("/health")
    public ResponseEntity<ApiUtil.ApiResult<String>> healthCheck() {
        return ResponseEntity.ok(ApiUtil.success("Member API is healthy!"));
    }
}
