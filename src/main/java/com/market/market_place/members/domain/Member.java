package com.market.market_place.members.domain;

import com.market.market_place.members.dtos.MemberRegisterRequest;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "member_tb")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(nullable = false, length = 255)
    private String password;

    @Column(unique = true, nullable = false, length = 50)
    private String loginId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MemberRole role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MemberStatus status; // 회원 상태 (활성, 탈퇴, 정지)

    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private MemberProfile memberProfile;

    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private MemberActivity memberActivity;

    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private MemberAuth memberAuth;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public enum MemberRole {
        ADMIN, USER
    }

    //== 정적 팩토리 메서드 ==//
    // 회원가입 요청 정보를 바탕으로 완전한 Member 객체를 생성합니다.
    public static Member from(MemberRegisterRequest dto, PasswordEncoder passwordEncoder) {
        Member member = Member.builder()
                .loginId(dto.getLoginId())
                .password(passwordEncoder.encode(dto.getPassword()))
                .role(MemberRole.USER)
                .status(MemberStatus.ACTIVE) // 신규 회원은 항상 활성 상태로 시작
                .build();

        member.setMemberProfile(MemberProfile.builder().build());
        member.setMemberActivity(MemberActivity.builder().build());
        member.setMemberAuth(MemberAuth.builder()
                .phoneNumber(dto.getPhoneNumber())
                .telecom(dto.getTelecom())
                .build());

        return member;
    }

    //== 연관관계 편의 메서드 ==//
    // 양방향 연관관계에서 데이터의 일관성을 유지합니다.
    public void setMemberProfile(MemberProfile memberProfile) {
        this.memberProfile = memberProfile;
        memberProfile.setMember(this);
    }

    public void setMemberActivity(MemberActivity memberActivity) {
        this.memberActivity = memberActivity;
        memberActivity.setMember(this);
    }

    public void setMemberAuth(MemberAuth memberAuth) {
        this.memberAuth = memberAuth;
        memberAuth.setMember(this);
    }

    //== 엔티티 비즈니스 로직 ==//
    // Member 엔티티와 직접 관련된 비즈니스 로직을 처리합니다.
    public void updatePassword(String password) {
        this.password = password;
    }

    public void withdraw() {
        this.status = MemberStatus.WITHDRAWN;
    }

    public void ban() {
        this.status = MemberStatus.BANNED;
    }
}
