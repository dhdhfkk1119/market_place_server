package com.market.market_place.members.services;

import com.market.market_place._core._exception.Exception400;
import com.market.market_place._core._exception.Exception401;
import com.market.market_place._core._utils.JwtUtil;
import com.market.market_place.members.domain.Member;
import com.market.market_place.members.domain.MemberStatus;
import com.market.market_place.members.dtos.*;
import com.market.market_place.members.repositories.MemberAuthRepository;
import com.market.market_place.members.repositories.MemberRepository;
import com.market.market_place.terms.dtos.AgreeTermsRequestDto;
import com.market.market_place.terms.services.TermsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberAuthService {

    private final MemberRepository memberRepository;
    private final MemberAuthRepository memberAuthRepository; // 이메일 중복 확인을 위해 주입
    private final PasswordEncoder passwordEncoder;
    private final TermsService termsService;
    private final MemberService memberService; // Core 서비스 의존

    // 회원가입
    @Transactional
    public MemberRegisterResponse registerMember(MemberRegisterRequest request) {
        // 1. 아이디 중복 확인
        if (memberRepository.findByLoginId(request.getLoginId()).isPresent()) {
            throw new Exception400("이미 사용 중인 아이디입니다.");
        }

        // 2. 이메일 중복 확인
        if (memberAuthRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new Exception400("이미 사용 중인 이메일입니다.");
        }

        // 3. 엔티티 생성 및 저장
        Member newMember = Member.from(request, passwordEncoder);
        Member savedMember = memberRepository.save(newMember);

        // 4. 약관 동의 처리
        AgreeTermsRequestDto agreeTermsRequest = new AgreeTermsRequestDto();
        agreeTermsRequest.setAgreedTermIds(request.getAgreedTermIds());
        termsService.agreeTerms(agreeTermsRequest, savedMember);

        return new MemberRegisterResponse(savedMember);
    }

    // 로그인
    public MemberLoginResult login(MemberLoginRequest request) {
        Member member = memberRepository.findByLoginId(request.getLoginId())
                .orElseThrow(() -> new Exception401("아이디 또는 비밀번호가 일치하지 않습니다."));

        if (member.getStatus() != MemberStatus.ACTIVE) {
            throw new Exception401("이미 탈퇴했거나 정지된 계정입니다.");
        }

        if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            throw new Exception401("아이디 또는 비밀번호가 일치하지 않습니다.");
        }

        String jwt = JwtUtil.createToken(member);
        MemberLoginResponse responseDTO = new MemberLoginResponse(member);

        return new MemberLoginResult(jwt, responseDTO);
    }

    // 비밀번호 변경
    @Transactional
    public void changePassword(Long memberId, ChangePasswordRequest request) {
        Member member = memberService.findMember(memberId);

        if (!passwordEncoder.matches(request.getCurrentPassword(), member.getPassword())) {
            throw new Exception401("현재 비밀번호가 일치하지 않습니다.");
        }

        String newEncodedPassword = passwordEncoder.encode(request.getNewPassword());
        member.updatePassword(newEncodedPassword);
    }
}
