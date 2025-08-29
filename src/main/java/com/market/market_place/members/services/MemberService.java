package com.market.market_place.members.services;

import com.market.market_place._core._exception.Exception400;
import com.market.market_place._core._exception.Exception401;
import com.market.market_place._core._exception.Exception404;
import com.market.market_place.members.domain.Member;
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
        // 1. 요청 데이터 유효성 검사
        if (request.getIsVerified() == null || !request.getIsVerified()) {
            throw new Exception400("전화번호 인증이 완료되지 않았습니다.");
        }
        if (memberRepository.findByLoginId(request.getLoginId()).isPresent()) {
            throw new Exception400("이미 사용 중인 아이디입니다.");
        }

        // 2. 엔티티 생성 책임을 Member의 팩토리 메서드에 위임
        Member newMember = Member.from(request, passwordEncoder);
        Member savedMember = memberRepository.save(newMember);

        // 3. 약관 동의 처리 책임을 TermsService에 위임
        AgreeTermsRequestDto agreeTermsRequest = new AgreeTermsRequestDto();
        agreeTermsRequest.setAgreedTermIds(request.getAgreedTermIds());
        termsService.agreeTerms(agreeTermsRequest, savedMember);

        // 4. DTO로 변환하여 반환
        return new MemberRegisterResponse(savedMember);
    }

    // 회원 정보 수정 (닉네임 / 프로필 이미지)
    @Transactional
    public Member updateMember(Long memberId, MemberUpdateRequest request) {
        // 1. 수정할 회원 엔티티 조회
        Member member = findMember(memberId);

        // 2. 업데이트 책임을 MemberProfile 엔티티의 인스턴스 메서드에 위임
        member.getMemberProfile().updateFrom(request);

        return member;
    }

    // 로그인
    public Member login(MemberLoginRequest request) {
        // 1. 아이디로 회원 조회
        Member member = memberRepository.findByLoginId(request.getLoginId())
                .orElseThrow(() -> new Exception401("아이디 또는 비밀번호가 일치하지 않습니다."));
        
        // 2. 비밀번호 일치 여부 확인
        if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            throw new Exception401("아이디 또는 비밀번호가 일치하지 않습니다.");
        }
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

    // 회원 전체 조회 (관리자용)
    public List<Member> findAllMembers() {
        return memberRepository.findAll();
    }

    // 회원 삭제 (관리자용)
    @Transactional
    public void deleteMember(Long memberId) {
        if (!memberRepository.existsById(memberId)) {
            throw new Exception404("삭제할 회원을 찾을 수 없습니다.");
        }
        memberRepository.deleteById(memberId);
    }
}
