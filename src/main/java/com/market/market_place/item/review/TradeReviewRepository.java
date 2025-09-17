package com.market.market_place.item.review;

import com.market.market_place.item.review.TradeReview;
import com.market.market_place.item.status.Trade;
import com.market.market_place.members.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TradeReviewRepository extends JpaRepository<TradeReview,Long> {

    @Query("SELECT AVG(tr.rating) FROM TradeReview tr JOIN tr.trade t WHERE t.item.id = :itemId")
    Double findAverageRatingByItemId(@Param("itemId") Long itemId);

    boolean existsByTradeAndReviewer(Trade trade, Member reviewer);
}