package com.market.market_place.item.praise;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PraiseRepository extends JpaRepository<Praise, Long>, QuerydslPredicateExecutor<Praise> {
<<<<<<< HEAD
    boolean existsByPraiserIdAndPraisedMemberIdAndTradeId(Long praiserId, Long praisedMemberId, Long tradeId);
}
=======
    boolean existsByPraiserIdAndTradeId(Long praiserId, Long tradeId);


}
>>>>>>> f-board
