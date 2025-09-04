package com.market.market_place.members.services;

import com.market.market_place._core._exception.Exception404;
import com.market.market_place.members.domain.Member;
import com.market.market_place.members.dto_auth.MemberRegisterResponse;
import com.market.market_place.members.repositories.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberAdminService {

    private final MemberRepository memberRepository;
    private final MemberService memberService; // Core 서비스 의존

    // 회원 정지 (관리자용)
    @Transactional
    public void banMember(Long memberId) {
        log.info("관리자 기능: 회원 정지 처리 시작. 대상 ID: {}", memberId);
        Member member = memberService.findMember(memberId);
        member.ban();
        log.info("관리자 기능: 회원 정지 처리 완료. 대상 ID: {}", memberId);
    }

    // 회원 상세 조회 (Login ID 기준, 관리자용)
    public MemberRegisterResponse findMemberByLoginId(String loginId) {
        log.debug("관리자 기능: 로그인 ID로 회원 조회. 로그인 ID: {}", loginId);
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new Exception404("해당 회원을 찾을 수 없습니다."));
        return new MemberRegisterResponse(member);
    }

    // 회원 전체 조회 (관리자용)
    public List<MemberRegisterResponse> findAllMembers() {
        log.debug("관리자 기능: 전체 회원 목록 조회");
        List<Member> members = memberRepository.findAll();
        return members.stream()
                .map(MemberRegisterResponse::new)
                .collect(Collectors.toList());
    }

    // 회원 삭제 (관리자용, 물리적 삭제)
    @Transactional
    public void deleteMember(Long memberId) {
        log.warn("관리자 기능: 회원 물리적 삭제 처리 시작. 대상 ID: {}", memberId);
        if (!memberRepository.existsById(memberId)) {
            throw new Exception404("삭제할 회원을 찾을 수 없습니다.");
        }
        memberRepository.deleteById(memberId);
        log.warn("관리자 기능: 회원 물리적 삭제 처리 완료. 대상 ID: {}", memberId);
    }
}
