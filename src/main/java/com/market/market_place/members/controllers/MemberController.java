package com.market.market_place.members.controllers;

import com.market.market_place._core._utils.JwtUtil;
import com.market.market_place.members.domain.Member;
import com.market.market_place.members.dtos.*;
import com.market.market_place.members.services.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
    private final JwtUtil jwtUtil; // JwtUtil 주입

    // 회원가입
    @PostMapping("/register")
    public ResponseEntity<MemberRegisterResponse> registerMember(@Valid @RequestBody MemberRegisterRequest request) {
        Member newMember = memberService.registerMember(request);
        MemberRegisterResponse response = new MemberRegisterResponse(newMember);
        URI location = URI.create("/api/members/" + newMember.getId());
        return ResponseEntity.created(location).body(response);
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<MemberLoginResponse> login(@RequestBody MemberLoginRequest request) {
        // 1. 사용자 인증
        Member member = memberService.login(request);

        // 2. JWT 토큰 생성 (새로운 JwtUtil 사용)
        String token = JwtUtil.createToken(member);

        // 3. 응답 DTO 생성 및 반환
        MemberLoginResponse response = new MemberLoginResponse(member, token);
        return ResponseEntity.ok(response);
    }

    // 회원 정보 수정
    @PatchMapping("/{id}")
    public ResponseEntity<MemberUpdateResponse> updateMember(@PathVariable Long id, @Valid @RequestBody MemberUpdateRequest request) {
        Member updatedMember = memberService.updateMember(id, request);
        MemberUpdateResponse response = new MemberUpdateResponse(updatedMember);
        return ResponseEntity.ok(response);
    }

    // 회원 상세 조회
    @GetMapping("/{id}")
    public ResponseEntity<MemberRegisterResponse> getMember(@PathVariable Long id) {
        Member member = memberService.findMember(id);
        MemberRegisterResponse response = new MemberRegisterResponse(member);
        return ResponseEntity.ok(response);
    }

    // 회원 전체 조회
    @GetMapping
    public ResponseEntity<List<MemberRegisterResponse>> getAllMembers() {
        List<Member> members = memberService.findAllMembers();
        List<MemberRegisterResponse> response = members.stream()
                .map(MemberRegisterResponse::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    // 회원 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMember(@PathVariable Long id) {
        memberService.deleteMember(id);
        return ResponseEntity.noContent().build();
    }

    // 상태 확인용
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Member API is healthy!");
    }
}
