package com.market.market_place.members.services;

import com.market.market_place._core._exception.Exception400;
import com.market.market_place._core._exception.Exception401;
import com.market.market_place._core._exception.Exception404;
import com.market.market_place.members.domain.Member;
import com.market.market_place.members.domain.MemberActivity;
import com.market.market_place.members.domain.MemberAuth;
import com.market.market_place.members.domain.MemberProfile;
import com.market.market_place.members.dtos.MemberLoginRequest;
import com.market.market_place.members.dtos.MemberRegisterRequest;
import com.market.market_place.members.dtos.MemberUpdateRequest;
import com.market.market_place.members.repositories.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    // 회원가입
    @Transactional
    public Member registerMember(MemberRegisterRequest request) {
        if (memberRepository.findByLoginId(request.getLoginId()).isPresent()) {
            throw new Exception400("이미 사용 중인 아이디입니다.");
        }

        // 1. Member 엔티티 생성 (인증 정보 중심)
        Member newMember = Member.builder()
                .loginId(request.getLoginId())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Member.MemberRole.USER)
                .build();

        // 2. 연관 엔티티 생성
        MemberProfile memberProfile = MemberProfile.builder().build();
        MemberActivity memberActivity = MemberActivity.builder().build();
        MemberAuth memberAuth = MemberAuth.builder()
                .phoneNumber(request.getPhoneNumber())
                .build();

        // 3. 연관관계 설정 (CascadeType.ALL 덕분에 Member만 저장해도 모두 저장됨)
        newMember.setMemberProfile(memberProfile);
        newMember.setMemberActivity(memberActivity);
        newMember.setMemberAuth(memberAuth);

        // 4. 저장
        return memberRepository.save(newMember);
    }

    // 로그인
    public Member login(MemberLoginRequest request) {
        Member member = memberRepository.findByLoginId(request.getLoginId())
                .orElseThrow(() -> new Exception401("아이디 또는 비밀번호가 일치하지 않습니다."));

        if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            throw new Exception401("아이디 또는 비밀번호가 일치하지 않습니다.");
        }

        return member;
    }

    // 회원 정보 수정 (이름/닉네임)
    @Transactional
    public Member updateMember(Long memberId, MemberUpdateRequest request) {
        Member member = findMember(memberId);
        // 이제 이름은 MemberProfile이 관리
        member.getMemberProfile().updateName(request.getName());
        return member;
    }

    // 회원 상세 조회 (ID 기준)
    public Member findMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new Exception404("해당 회원을 찾을 수 없습니다."));
    }

    // 회원 상세 조회 (Login ID 기준)
    public Member findMemberByLoginId(String loginId) {
        return memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new Exception404("해당 회원을 찾을 수 없습니다."));
    }

    // 회원 전체 조회
    public List<Member> findAllMembers() {
        return memberRepository.findAll();
    }

    // 회원 삭제
    @Transactional
    public void deleteMember(Long memberId) {
        if (!memberRepository.existsById(memberId)) {
            throw new Exception404("삭제할 회원을 찾을 수 없습니다.");
        }
        memberRepository.deleteById(memberId);
    }
}
