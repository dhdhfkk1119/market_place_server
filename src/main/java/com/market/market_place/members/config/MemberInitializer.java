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
                    .role(Role.ADMIN)
                    .status(MemberStatus.ACTIVE)
                    .build();

            MemberProfile adminProfile = MemberProfile.builder()
                    .name("관리자")
                    .build();
            MemberActivity adminActivity = MemberActivity.builder().build();
            MemberAuth adminAuth = MemberAuth.builder()
                    .email("admin@market.com")
                    .build();

            admin.setMemberProfile(adminProfile);
            admin.setMemberActivity(adminActivity);
            admin.setMemberAuth(adminAuth);

            memberRepository.save(admin);
        }

        // 테스트용 일반 멤버 계정이 없으면 생성
        if (memberRepository.findByLoginId("user1").isEmpty()) {
            Member user = Member.builder()
                    .loginId("user1")
                    .password(passwordEncoder.encode("user1234"))
                    .role(Role.USER)
                    .status(MemberStatus.ACTIVE)
                    .build();

            MemberProfile userProfile = MemberProfile.builder()
                    .name("테스트유저")
                    .build();
            MemberActivity userActivity = MemberActivity.builder().build();
            MemberAuth userAuth = MemberAuth.builder()
                    .email("user1@market.com")
                    .build();

            user.setMemberProfile(userProfile);
            user.setMemberActivity(userActivity);
            user.setMemberAuth(userAuth);

            memberRepository.save(user);
        }
    }
}
