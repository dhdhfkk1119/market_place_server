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

    // 사용자 전화번호 (고유 값)
    @Column(unique = true)
    private String phoneNumber;

    // 통신사 정보를 저장하는 필드
    private String telecom;

    // 전화번호 인증 완료 시각
    private LocalDateTime verifiedAt;
}
