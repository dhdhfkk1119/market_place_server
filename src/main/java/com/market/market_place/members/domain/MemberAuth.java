package com.market.market_place.members.domain;

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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(unique = true)
    private String phoneNumber;

    private String ssnPrefix; // 주민번호 앞자리

    private String ssnSuffix; // 주민번호 뒷자리 (암호화 필요)

    private LocalDateTime verifiedAt; // 인증 완료 시각
}