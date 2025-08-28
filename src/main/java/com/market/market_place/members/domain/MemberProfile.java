package com.market.market_place.members.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter // setMember를 위해 임시로 허용
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "member_profile_tb")
public class MemberProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(length = 100)
    private String name; // 닉네임

    @Lob // Base64 인코딩된 긴 텍스트 저장을 위해 @Lob 사용
    private String profileImageBase64;

    private Double temperature = 36.5; // 매너 온도 초기값

    private Double retradeRate;

    private Double responseRate;

    @UpdateTimestamp
    private LocalDateTime lastActiveAt;

    //== 비즈니스 로직 ==//
    public void updateName(String name) {
        this.name = name;
    }
}
