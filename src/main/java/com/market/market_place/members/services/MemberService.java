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
import com.market.market_place.members.dtos.MemberRegisterResponse;
import com.market.market_place.members.dtos.MemberUpdateRequest;
import com.market.market_place.members.repositories.MemberRepository;
import com.market.market_place.terms.dtos.AgreeTermsRequestDto;
import com.market.market_place.terms.services.TermsService;
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
    private final TermsService termsService;

    // 회원가입
    @Transactional
    public MemberRegisterResponse registerMember(MemberRegisterRequest request) {
        // 1. 전화번호 인증 여부 확인
        if (request.getIsVerified() == null || !request.getIsVerified()) {
            throw new Exception400("전화번호 인증이 완료되지 않았습니다.");
        }
        // 2. 아이디 중복 확인
        if (memberRepository.findByLoginId(request.getLoginId()).isPresent()) {
            throw new Exception400("이미 사용 중인 아이디입니다.");
        }

        // 3. Member 엔티티 생성
        Member newMember = Member.builder()
                .loginId(request.getLoginId())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Member.MemberRole.USER)
                .build();

        // 4. 연관 엔티티 생성 및 설정 (연관관계 편의 메소드 사용)
        newMember.setMemberProfile(MemberProfile.builder().build());
        newMember.setMemberActivity(MemberActivity.builder().build());
        // telecom 정보 추가
        newMember.setMemberAuth(MemberAuth.builder()
                .phoneNumber(request.getPhoneNumber())
                .telecom(request.getTelecom())
                .build());

        // 5. 회원 정보 저장
        Member savedMember = memberRepository.save(newMember);

        // 6. 약관 동의 처리
        AgreeTermsRequestDto agreeTermsRequest = new AgreeTermsRequestDto();
        agreeTermsRequest.setAgreedTermIds(request.getAgreedTermIds());
        termsService.agreeTerms(agreeTermsRequest, savedMember);

        // 7. 응답 DTO 생성 및 반환
        return new MemberRegisterResponse(savedMember);
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
