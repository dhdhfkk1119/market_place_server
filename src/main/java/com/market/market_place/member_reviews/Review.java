package com.market.market_place.member_reviews;

import com.market.market_place.members.domain.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 리뷰 엔티티
 * - reviewer: 리뷰 작성자
 * - reviewed: 리뷰 대상자
 * - rating: 별점 (1~5)
 * - comment: 리뷰 내용
 * - createdAt: 생성일시
 *
 * reviewer_id와 reviewed_id의 조합에 유니크 제약을 걸어
 * 한 회원이 다른 회원에게 1개의 리뷰만 남길 수 있도록 설계
 */
@Entity
@Table(name = "reviews", uniqueConstraints = {@UniqueConstraint(columnNames = {"reviewer_id", "reviewed_id"})})
@Getter
public class Review {
    /** 리뷰 PK (기본키) */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 리뷰 작성자 (회원) */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewer_id", nullable = false)
    private Member reviewer;

    /** 리뷰 대상자 (회원) */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewed_id", nullable = false)
    private Member reviewed;

    /** 별점 (1~5) */
    @Column(nullable = false)
    private Integer rating;

    /** 리뷰 내용 */
    @Column(length = 500)
    private String comment;

    /** 생성일시 */
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /** 기본 생성자 */
    public Review() {}

    /** 빌더 생성자 */
    @Builder
    public Review(Member reviewer, Member reviewed, Integer rating, String comment) {
        this.reviewer = reviewer;
        this.reviewed = reviewed;
        this.rating = rating;
        this.comment = comment;
    }

    /** 별점 수정 */
    public void setRating(Integer rating) {
        this.rating = rating;
    }

    /** 리뷰 내용 수정 */
    public void setComment(String comment) {
        this.comment = comment;
    }
}
