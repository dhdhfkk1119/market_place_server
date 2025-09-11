package com.market.market_place.members.domain;

import com.market.market_place.members.dto_auth.MemberRegisterRequest;
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
@EqualsAndHashCode(of = "id")
@Table(name = "member_tb")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // pk

    @Setter
    @Column(nullable = true, length = 255)
    private String password;

    @Column(unique = true, nullable = true, length = 50)
    private String loginId; //로그인용 아이디

    @Column 
    private String address; // 단일 주소

    // 사용자 이메일 (고유 값)
    @Column(unique = true, nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private Provider provider; // 소셜 로그인 제공자

    @Column(nullable = true, length = 255)
    private String providerId; // 소셜 로그인 제공자의 고유 ID

    // 이메일 인증 완료 시각
    private LocalDateTime emailVerifiedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role; // 독립된 Role 열거형

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MemberStatus status; // 회원 상태 (활성, 탈퇴, 정지)

    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private MemberProfile memberProfile;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    private double totalReviewScore;
    private int reviewCount;
    private double mannerScore;
    private int tradeCount;

    //== 정적 팩토리 메서드 ==//
    // 회원가입 요청 정보를 바탕으로 완전한 Member 객체를 생성합니다.
    public static Member from(MemberRegisterRequest dto, PasswordEncoder passwordEncoder) {
        Member member = Member.builder()
                .loginId(dto.getLoginId())
                .password(passwordEncoder.encode(dto.getPassword()))
                .address(dto.getAddress()) // 주소 정보 설정 추가
                .email(dto.getEmail())
                .role(Role.USER) // 독립된 Role 열거형을 사용하도록 수정
                .status(MemberStatus.ACTIVE) // 신규 회원은 항상 활성 상태로 시작
                .totalReviewScore(0.0)
                .reviewCount(0)
                .mannerScore(0.0)
                .tradeCount(0)
                .build();

        member.setMemberProfile(MemberProfile.builder().build());

        return member;
    }

    //== 연관관계 편의 메서드 ==//
    // 양방향 연관관계에서 데이터의 일관성을 유지합니다.
    public void setMemberProfile(MemberProfile memberProfile) {
        this.memberProfile = memberProfile;
        memberProfile.setMember(this);
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

    public void addReviewScore(double score) {
        this.totalReviewScore += score;
        this.reviewCount += 1;
    }

    public void addMannerScore(double score) {
        this.mannerScore += score;
    }

    public void incrementTradeCount() {
        this.tradeCount += 1;
    }
}
