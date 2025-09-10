package com.market.market_place.members.services;

import com.market.market_place._core._exception.Exception404;
import com.market.market_place.members.domain.Member;
import com.market.market_place.members.dto_profile.MemberUpdateRequest;
import com.market.market_place.members.dto_profile.MemberUpdateResponse;
import com.market.market_place.members.dto_profile.MyInfoResponse;
import com.market.market_place.members.repositories.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    // 회원 정보 수정 (닉네임 / 프로필 이미지)
    @Transactional
    public MemberUpdateResponse updateMember(Long memberId, MemberUpdateRequest request) {
        log.info("회원 정보 수정 시작. 사용자 ID: {}", memberId);
        Member member = findMember(memberId);
        member.getMemberProfile().updateFrom(request);
        log.info("회원 정보 수정 완료. 사용자 ID: {}", memberId);
        return new MemberUpdateResponse(member);
    }

    // 회원 탈퇴 (논리적 삭제)
    @Transactional
    public void withdrawMember(Long memberId) {
        log.info("회원 탈퇴 처리 시작. 사용자 ID: {}", memberId);
        Member member = findMember(memberId);
        member.withdraw();
        log.info("회원 탈퇴 처리 완료. 사용자 ID: {}", memberId);
    }

    // 내 정보 조회
    public MyInfoResponse getMyInfo(Long memberId) {
        log.debug("내 정보 조회. 사용자 ID: {}", memberId);
        Member member = findMember(memberId);
        return new MyInfoResponse(member);
    }

    // 회원 상세 조회 (ID 기준, 다른 서비스에서 사용하기 위한 public 메서드)
    public Member findMember(Long memberId) {
        log.debug("ID로 회원 조회. 사용자 ID: {}", memberId);
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new Exception404("해당 회원을 찾을 수 없습니다."));
    }
}
