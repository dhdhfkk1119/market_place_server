package com.market.market_place.members.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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

    //== 연관관계 편의 메소드 ==//
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

    //== 비즈니스 로직 ==//
    public void updatePassword(String password) {
        this.password = password;
    }
}
