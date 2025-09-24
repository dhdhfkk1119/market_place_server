package com.market.market_place.member_reviews;

import com.market.market_place.members.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    @Query("SELECT r FROM Review r JOIN FETCH r.reviewer reviewer JOIN FETCH reviewer.memberProfile WHERE r.reviewed.id = :memberId")
    List<Review> findByReviewedId(@Param("memberId") Long memberId);

    boolean existsByReviewerAndReviewed(Member reviewer, Member reviewed);
}

