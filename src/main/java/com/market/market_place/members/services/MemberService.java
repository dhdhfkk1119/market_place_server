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

//    /**
//     * @deprecated 이 메서드의 책임은 MemberAdminService로 이전되었으나, 다른 도메인과의 호환성을 위해 임시로 남겨둡니다.
//     *             곧 제거될 예정이므로 새로운 코드는 이 메서드를 사용하지 마세요.
//     *             대신 sessionUser.getId()를 사용하여 findMember(Long id)를 호출하는 것을 권장합니다.
//     */
//    @Deprecated
//    public Member findMemberByLoginId(String loginId) {
//        log.warn("Deprecated 메서드 호출됨: findMemberByLoginId. 호출자: CommentController");
//        return memberRepository.findByLoginId(loginId)
//                .orElseThrow(() -> new Exception404("해당 회원을 찾을 수 없습니다."));
//    }
}
