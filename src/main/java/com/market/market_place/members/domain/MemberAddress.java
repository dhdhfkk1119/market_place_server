package com.market.market_place.members.domain;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "member_address_tb")
public class MemberAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    //@Column(nullable = false)
    @Column
    private String addressBasic; // 기본 주소 (예: "서울시 강남구")

    //@Column(nullable = false)
    @Column
    private String addressDetail; // 상세 주소 (예: "역삼동")

//    @Column(nullable = false)
//    private boolean isCurrent; // 현재 설정된 주소인지 여부
}