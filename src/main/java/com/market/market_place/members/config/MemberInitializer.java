package com.market.market_place.members.config;

import com.market.market_place.members.domain.*;
import com.market.market_place.members.repositories.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

// 개발(dev) 또는 로컬(local) 환경에서만 실행되도록 프로필 설정
@Profile({"dev", "local"})
@Component
@RequiredArgsConstructor
public class MemberInitializer implements CommandLineRunner {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        // 관리자 계정이 없으면 생성
        if (memberRepository.findByLoginId("admin").isEmpty()) {
            Member admin = Member.builder()
                    .loginId("admin")
                    .password(passwordEncoder.encode("admin1234"))
                    .address("서울시 강남구") // 주소 정보 추가
                    .email("admin@market.com")
                    .role(Role.ADMIN)
                    .status(MemberStatus.ACTIVE)
                    .provider(Provider.MARKIT)
                    .build();

            MemberProfile adminProfile = MemberProfile.builder()
                    .name("관리자")
                    .build();

            admin.setMemberProfile(adminProfile);

            memberRepository.save(admin);
        }

        // 테스트용 일반 멤버 계정이 없으면 생성
        if (memberRepository.findByLoginId("user1").isEmpty()) {
            Member user = Member.builder()
                    .loginId("user1")
                    .password(passwordEncoder.encode("user1234"))
                    .address("부산시 연제구") // 주소 정보 추가
                    .email("choongecho@gmail.com")
                    .role(Role.USER)
                    .status(MemberStatus.ACTIVE)
                    .provider(Provider.MARKIT)
                    .build();

            MemberProfile userProfile = MemberProfile.builder()
                    .name("테스트유저")
                    .build();

            user.setMemberProfile(userProfile);

            memberRepository.save(user);
        }

        // 테스트용 일반 멤버 계정 2가 없으면 생성
        if (memberRepository.findByLoginId("user2").isEmpty()) {
            Member user2 = Member.builder()
                    .loginId("user2")
                    .password(passwordEncoder.encode("user1234"))
                    .address("경기도 성남시")
                    .email("user2@market.com")
                    .role(Role.USER)
                    .status(MemberStatus.ACTIVE)
                    .provider(Provider.MARKIT)
                    .build();

            MemberProfile user2Profile = MemberProfile.builder()
                    .name("테스트유저2")
                    .build();

            user2.setMemberProfile(user2Profile);
            memberRepository.save(user2);
        }

        // 테스트용 탈퇴 멤버 계정이 없으면 생성
        if (memberRepository.findByLoginId("withdrawn_user").isEmpty()) {
            Member withdrawnUser = Member.builder()
                    .loginId("withdraw")
                    .password(passwordEncoder.encode("withdraw"))
                    .address("서울시 마포구")
                    .email("withdrawn@market.com")
                    .role(Role.USER)
                    .status(MemberStatus.WITHDRAWN) // 탈퇴 상태
                    .provider(Provider.MARKIT)
                    .build();

            MemberProfile withdrawnProfile = MemberProfile.builder()
                    .name("탈퇴한유저")
                    .build();

            withdrawnUser.setMemberProfile(withdrawnProfile);
            memberRepository.save(withdrawnUser);
        }

        // 테스트용 정지 멤버 계정이 없으면 생성
        if (memberRepository.findByLoginId("banned_user").isEmpty()) {
            Member bannedUser = Member.builder()
                    .loginId("banned")
                    .password(passwordEncoder.encode("banned"))
                    .address("서울시 서대문구")
                    .email("banned@market.com")
                    .role(Role.USER)
                    .status(MemberStatus.BANNED) // 정지 상태
                    .provider(Provider.MARKIT)
                    .build();

            MemberProfile bannedProfile = MemberProfile.builder()
                    .name("정지된유저")
                    .build();

            bannedUser.setMemberProfile(bannedProfile);
            memberRepository.save(bannedUser);
        }
    }
}
