package com.market.market_place.item.status;

import com.market.market_place.item.core.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TradeRepository extends JpaRepository<Trade, Long> {
    Optional<Trade> findByItem(Item item);
}
