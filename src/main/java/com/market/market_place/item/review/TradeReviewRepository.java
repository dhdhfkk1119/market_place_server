package com.market.market_place.item.review;

import com.market.market_place.item.status.Trade;
import com.market.market_place.members.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

// 거래 리뷰 레포지토리 (DB 접근)
@Repository
public interface TradeReviewRepository extends JpaRepository<TradeReview, Long> {
    // 평균 평점 조회
    @Query("SELECT AVG(tr.rating) FROM TradeReview tr JOIN tr.trade t WHERE t.item.id = :itemId")
    Double findAverageRatingByItemId(@Param("itemId") Long itemId);

    // 거래+작성자 중복 체크
    boolean existsByTradeAndReviewer(Trade trade, Member reviewer);

    // 판매자(memberId)의 판매 완료 리뷰 리스트 조회
    @Query("SELECT tr FROM TradeReview tr " +
            "JOIN tr.trade t " +
            "JOIN t.item i " +
            "WHERE i.member.id = :memberId AND t.status = com.market.market_place.item.status.TradeStatus.SOLD")
    List<TradeReview> findSoldReviewsBySellerId(@Param("memberId") Long memberId);

}