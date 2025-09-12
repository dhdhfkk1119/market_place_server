package com.market.market_place.item.status;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TradeRepository extends JpaRepository<Trade, Long> {
    @Query("SELECT AVG(t.averageScore) FROM Trade t WHERE t.item.id = :itemId")
    Double findAverageRatingByItemId(@Param("itemId") Long itemId);
}
