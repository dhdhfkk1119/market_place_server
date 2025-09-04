package com.market.market_place.item.review;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TradeReviewRepository extends JpaRepository<TradeReview,Long> {

    // 특정 거래에 대한 모든 후기 평점의 평균 계산
    @Query("SELECT AVG(tr.rating) FROM TradeReview tr WHERE tr.trade.id = :tradeId")
    Double findAverageRatingByTradeId(@Param("tradeId") Long tradeId);

}
