package com.market.market_place.members.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "member_auth_tb")
public class MemberAuth {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore // 양방향 관계에서 무한 순환 참조 방지
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    // 사용자 이메일 (고유 값)
    @Column(unique = true, nullable = false)
    private String email;

    // 이메일 인증 완료 시각
    private LocalDateTime emailVerifiedAt;
}
