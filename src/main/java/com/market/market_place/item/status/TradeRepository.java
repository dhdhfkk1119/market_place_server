package com.market.market_place.item.status;

import com.market.market_place.item.core.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TradeRepository extends JpaRepository<Trade, Long> {
    Optional<Trade> findByItem(Item item);

    // 구매내역: buyer_id 기준으로 Trade 엔티티 리스트 조회
    List<Trade> findByBuyerId(Long buyerId);


}
