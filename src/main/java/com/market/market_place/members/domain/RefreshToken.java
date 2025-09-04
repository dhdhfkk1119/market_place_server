package com.market.market_place.members.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "refresh_token_tb")
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Long memberId 대신 Member 객체를 직접 참조
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false, unique = true)
    private Member member;

    @Column(nullable = false)
    private String tokenValue;

    @Builder
    public RefreshToken(Member member, String tokenValue) {
        this.member = member;
        this.tokenValue = tokenValue;
    }

    // 토큰 값을 업데이트하는 비즈니스 로직
    public void updateTokenValue(String newTokenValue) {
        this.tokenValue = newTokenValue;
    }
}
