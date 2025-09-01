package com.market.market_place.members.services;

import com.market.market_place._core._exception.Exception404;
import com.market.market_place.members.domain.Member;
import com.market.market_place.members.dtos.MemberRegisterResponse;
import com.market.market_place.members.dtos.MemberUpdateRequest;
import com.market.market_place.members.dtos.MemberUpdateResponse;
import com.market.market_place.members.dtos.MyInfoResponse;
import com.market.market_place.members.repositories.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    // 회원 정보 수정 (닉네임 / 프로필 이미지)
    @Transactional
    public MemberUpdateResponse updateMember(Long memberId, MemberUpdateRequest request) {
        Member member = findMember(memberId);
        member.getMemberProfile().updateFrom(request);
        return new MemberUpdateResponse(member);
    }

    // 회원 탈퇴 (논리적 삭제)
    @Transactional
    public void withdrawMember(Long memberId) {
        Member member = findMember(memberId);
        member.withdraw();
    }

    // 내 정보 조회
    public MyInfoResponse getMyInfo(Long memberId) {
        Member member = findMember(memberId);
        return new MyInfoResponse(member);
    }

    // 회원 상세 조회 (ID 기준, 다른 서비스에서 사용하기 위한 public 메서드)
    public Member findMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new Exception404("해당 회원을 찾을 수 없습니다."));
    }

    /**
     * @deprecated 이 메서드는 MemberAdminService로 책임이 이전될 예정이며,
     *             현재는 다른 도메인과의 호환성을 위해 임시로 유지됩니다.
     *             새로운 기능을 구현할 경우, 이 메서드를 사용하지 마십시오.
     *             대신 sessionUser.getId()를 사용하여 findMember(Long id)를 호출하는 것을 권장합니다.
     */
    @Deprecated
    public Member findMemberByLoginId(String loginId) {
        return memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new Exception404("해당 회원을 찾을 수 없습니다."));
    }
}
