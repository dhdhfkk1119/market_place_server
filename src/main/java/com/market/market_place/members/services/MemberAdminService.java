package com.market.market_place.members.services;

import com.market.market_place._core._exception.Exception404;
import com.market.market_place.members.domain.Member;
import com.market.market_place.members.dtos.MemberRegisterResponse;
import com.market.market_place.members.repositories.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberAdminService {

    private final MemberRepository memberRepository;
    private final MemberService memberService; // Core 서비스 의존

    // 회원 정지 (관리자용)
    @Transactional
    public void banMember(Long memberId) {
        Member member = memberService.findMember(memberId);
        member.ban();
    }

    // 회원 상세 조회 (Login ID 기준, 관리자용)
    public MemberRegisterResponse findMemberByLoginId(String loginId) {
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new Exception404("해당 회원을 찾을 수 없습니다."));
        return new MemberRegisterResponse(member);
    }

    // 회원 전체 조회 (관리자용)
    public List<MemberRegisterResponse> findAllMembers() {
        List<Member> members = memberRepository.findAll();
        return members.stream()
                .map(MemberRegisterResponse::new)
                .collect(Collectors.toList());
    }

    // 회원 삭제 (관리자용, 물리적 삭제)
    @Transactional
    public void deleteMember(Long memberId) {
        if (!memberRepository.existsById(memberId)) {
            throw new Exception404("삭제할 회원을 찾을 수 없습니다.");
        }
        memberRepository.deleteById(memberId);
    }
}
