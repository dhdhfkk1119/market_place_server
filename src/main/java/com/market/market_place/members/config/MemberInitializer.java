package com.market.market_place.members.config;

import com.market.market_place.members.domain.*;
import com.market.market_place.members.repositories.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

// 개발(dev) 또는 로컬(local) 환경에서만 실행되도록 프로필 설정
@Profile({"dev", "local"})
@Component
@Order(1) // 실행 순서 1번으로 지정
@RequiredArgsConstructor
public class MemberInitializer implements CommandLineRunner {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        createMemberIfNotExists("admin", "admin1234", "서울시 강남구", "admin@market.com", Role.ADMIN, MemberStatus.ACTIVE, "관리자");
        createMemberIfNotExists("withdraw", "withdraw", "서울시 마포구", "withdrawn@market.com", Role.USER, MemberStatus.WITHDRAWN, "탈퇴한유저");
        createMemberIfNotExists("banned", "banned", "서울시 서대문구", "banned@market.com", Role.USER, MemberStatus.BANNED, "밴당한 유저");
        createMemberIfNotExists("user1", "user1234", "부산시 연제구", "choongechobiz@gmail.com", Role.USER, MemberStatus.ACTIVE, "유저1");
        createMemberIfNotExists("user2", "user1234", "경기도 성남시", "user2@market.com", Role.USER, MemberStatus.ACTIVE, "유저2");
        createMemberIfNotExists("user3", "user1234", "부산광역시 해운대구", "user3@market.com", Role.USER, MemberStatus.ACTIVE, "유저3");
        createMemberIfNotExists("user4", "user1234", "대구광역시 달서구", "user4@market.com", Role.USER, MemberStatus.ACTIVE, "유저4");
        createMemberIfNotExists("user5", "user1234", "인천광역시 남동구", "user5@market.com", Role.USER, MemberStatus.ACTIVE, "유저5");
        createMemberIfNotExists("user6", "user1234", "광주광역시 북구", "user6@market.com", Role.USER, MemberStatus.ACTIVE, "유저6");
        createMemberIfNotExists("user7", "user1234", "대전광역시 유성구", "user7@market.com", Role.USER, MemberStatus.ACTIVE, "유저7");
        createMemberIfNotExists("user8", "user1234", "울산광역시 남구", "user8@market.com", Role.USER, MemberStatus.ACTIVE, "유저8");
        createMemberIfNotExists("user9", "user1234", "세종특별자치시 조치원읍", "user9@market.com", Role.USER, MemberStatus.ACTIVE, "유저9");
        createMemberIfNotExists("user10", "user1234", "경기도 수원시", "user10@market.com", Role.USER, MemberStatus.ACTIVE, "유저10");
        createMemberIfNotExists("user11", "user1234", "강원도 춘천시", "user11@market.com", Role.USER, MemberStatus.ACTIVE, "유저11");
        createMemberIfNotExists("user12", "user1234", "전라북도 전주시", "user12@market.com", Role.USER, MemberStatus.ACTIVE, "유저12");
        createMemberIfNotExists("user13", "user1234", "전라남도 목포시", "user13@market.com", Role.USER, MemberStatus.ACTIVE, "유저13");
    }

    private void createMemberIfNotExists(String loginId, String password, String address, String email, Role role, MemberStatus status, String profileName) {
        if (memberRepository.findByLoginId(loginId).isEmpty()) {
            Member member = Member.builder()
                    .loginId(loginId)
                    .password(passwordEncoder.encode(password))
                    .address(address)
                    .email(email)
                    .role(role)
                    .status(status)
                    .provider(Provider.MARKIT)
                    .build();

            MemberProfile profile = MemberProfile.builder()
                    .name(profileName)
                    .build();

            member.setMemberProfile(profile);
            memberRepository.save(member);
        }
    }
}
