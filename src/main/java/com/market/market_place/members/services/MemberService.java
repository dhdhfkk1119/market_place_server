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
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TermsService termsService;

    // 회원가입 (내용은 기존과 동일)
    @Transactional
    public MemberRegisterResponse registerMember(MemberRegisterRequest request) {
        if (request.getIsVerified() == null || !request.getIsVerified()) {
            throw new Exception400("전화번호 인증이 완료되지 않았습니다.");
        }
        if (memberRepository.findByLoginId(request.getLoginId()).isPresent()) {
            throw new Exception400("이미 사용 중인 아이디입니다.");
        }
        Member newMember = Member.builder()
                .loginId(request.getLoginId())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Member.MemberRole.USER)
                .build();
        newMember.setMemberProfile(MemberProfile.builder().build());
        newMember.setMemberActivity(MemberActivity.builder().build());
        newMember.setMemberAuth(MemberAuth.builder()
                .phoneNumber(request.getPhoneNumber())
                .telecom(request.getTelecom())
                .build());
        Member savedMember = memberRepository.save(newMember);
        AgreeTermsRequestDto agreeTermsRequest = new AgreeTermsRequestDto();
        agreeTermsRequest.setAgreedTermIds(request.getAgreedTermIds());
        termsService.agreeTerms(agreeTermsRequest, savedMember);
        return new MemberRegisterResponse(savedMember);
    }

    // 회원 정보 수정 (닉네임 / 프로필 이미지)
    @Transactional
    public Member updateMember(Long memberId, MemberUpdateRequest request) {
        Member member = findMember(memberId);
        MemberProfile memberProfile = member.getMemberProfile();

        if (StringUtils.hasText(request.getName())) {
            memberProfile.updateName(request.getName());
        }

        // Base64 문자열을 DB에 직접 저장
        if (StringUtils.hasText(request.getProfileImage())) {
            memberProfile.setProfileImageBase64(request.getProfileImage());
        }

        return member;
    }

    // --- 이하 다른 메서드들은 기존과 동일 ---
    public Member login(MemberLoginRequest request) {
        Member member = memberRepository.findByLoginId(request.getLoginId())
                .orElseThrow(() -> new Exception401("아이디 또는 비밀번호가 일치하지 않습니다."));
        if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            throw new Exception401("아이디 또는 비밀번호가 일치하지 않습니다.");
        }
        return member;
    }

    public Member findMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new Exception404("해당 회원을 찾을 수 없습니다."));
    }

    public Member findMemberByLoginId(String loginId) {
        return memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new Exception404("해당 회원을 찾을 수 없습니다."));
    }

    public List<Member> findAllMembers() {
        return memberRepository.findAll();
    }

    @Transactional
    public void deleteMember(Long memberId) {
        if (!memberRepository.existsById(memberId)) {
            throw new Exception404("삭제할 회원을 찾을 수 없습니다.");
        }
        memberRepository.deleteById(memberId);
    }
}
