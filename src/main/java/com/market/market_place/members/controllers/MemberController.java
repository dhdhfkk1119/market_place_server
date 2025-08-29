package com.market.market_place.members.controllers;

import com.market.market_place._core._exception.Exception403;
import com.market.market_place._core._utils.ApiUtil;
import com.market.market_place._core._utils.JwtUtil;
import com.market.market_place._core.auth.Auth;
import com.market.market_place.members.domain.Member;
import com.market.market_place.members.domain.Member.MemberRole;
import com.market.market_place.members.dtos.*;
import com.market.market_place.members.services.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;

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
        MemberLoginResponse response = new MemberLoginResponse(member);

        return ResponseEntity.ok()
                .header(JwtUtil.HEADER, JwtUtil.TOKEN_PREFIX + token)
                .body(ApiUtil.success(response));
    }

    // 회원 정보 수정
    @Auth(roles = {MemberRole.USER, MemberRole.ADMIN})
    @PatchMapping("/{id}")
    public ResponseEntity<ApiUtil.ApiResult<MemberUpdateResponse>> updateMember(
            @PathVariable Long id,
            @Valid @RequestBody MemberUpdateRequest request,
            @RequestAttribute("sessionUser") JwtUtil.SessionUser sessionUser) {

        // USER 권한일 경우, 본인의 정보만 수정 가능하도록 검증
        if (sessionUser.getRole() == MemberRole.USER && !sessionUser.getId().equals(id)) {
            throw new Exception403("본인의 정보만 수정할 수 있습니다.");
        }

        Member updatedMember = memberService.updateMember(id, request);
        MemberUpdateResponse response = new MemberUpdateResponse(updatedMember);
        return ResponseEntity.ok(ApiUtil.success(response));
    }

    // 회원 상세 조회 (loginId 기준)
    @Auth(roles = {MemberRole.USER, MemberRole.ADMIN})
    @GetMapping("/{loginId}")
    public ResponseEntity<ApiUtil.ApiResult<MemberRegisterResponse>> getMember(
            @PathVariable String loginId,
            @RequestAttribute("sessionUser") JwtUtil.SessionUser sessionUser) {
        // 모든 로그인 사용자가 조회 가능하므로 추가적인 권한 검증은 필요 없음
        Member member = memberService.findMemberByLoginId(loginId);
        MemberRegisterResponse response = new MemberRegisterResponse(member);
        return ResponseEntity.ok(ApiUtil.success(response));
    }

    // 회원 전체 조회
    @Auth(roles = MemberRole.ADMIN)
    @GetMapping
    public ResponseEntity<ApiUtil.ApiResult<List<MemberRegisterResponse>>> getAllMembers(
            @RequestAttribute("sessionUser") JwtUtil.SessionUser sessionUser) {
        // ADMIN 권한만 접근 가능하므로 추가적인 권한 검증은 필요 없음
        List<Member> members = memberService.findAllMembers();
        List<MemberRegisterResponse> response = members.stream()
                .map(MemberRegisterResponse::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiUtil.success(response));
    }

    // 회원 삭제
    @Auth(roles = MemberRole.ADMIN)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMember(
            @PathVariable Long id,
            @RequestAttribute("sessionUser") JwtUtil.SessionUser sessionUser) {
        // ADMIN 권한만 접근 가능하므로 추가적인 권한 검증은 필요 없음
        memberService.deleteMember(id);
        return ResponseEntity.noContent().build();
    }

    // 상태 확인용
    @GetMapping("/health")
    public ResponseEntity<ApiUtil.ApiResult<String>> healthCheck() {
        return ResponseEntity.ok(ApiUtil.success("Member API is healthy!"));
    }
}
