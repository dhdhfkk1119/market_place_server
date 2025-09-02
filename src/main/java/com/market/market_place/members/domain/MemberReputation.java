//package com.market.market_place.members.domain;
//
//import jakarta.persistence.*;
//import lombok.*;
//import org.hibernate.annotations.CreationTimestamp;
//
//import java.time.LocalDateTime;
//
//@Getter
//@Setter
//@Entity
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//@Table(name = "member_reputation_tb")
//public class MemberReputation {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "reviewer_id", nullable = false)
//    private Member reviewer; // 평가를 한 회원
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "reviewed_id", nullable = false)
//    private Member reviewed; // 평가를 받은 회원
//
//    @Enumerated(EnumType.STRING)
//    @Column(nullable = false)
//    private ReputationType type; // 좋아요, 싫어요 등
//
//    @CreationTimestamp
//    @Column(nullable = false)
//    private LocalDateTime createdAt;
//
//    public enum ReputationType {
//        LIKE, DISLIKE
//    }
//}
