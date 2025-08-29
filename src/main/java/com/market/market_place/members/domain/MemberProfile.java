package com.market.market_place.members.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.market.market_place.members.dtos.MemberUpdateRequest;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.util.StringUtils;

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

    @JsonIgnore // 양방향 관계에서 무한 순환 참조 방지
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

    //== 엔티티 비즈니스 로직 ==//
    // DTO를 기반으로 프로필 정보(닉네임, 이미지)를 업데이트합니다.
    public void updateFrom(MemberUpdateRequest dto) {
        if (StringUtils.hasText(dto.getName())) {
            this.name = dto.getName();
        }
        if (StringUtils.hasText(dto.getProfileImage())) {
            this.profileImageBase64 = dto.getProfileImage();
        }
    }
}
