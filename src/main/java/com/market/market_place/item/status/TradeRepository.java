package com.market.market_place.item.status;

import com.market.market_place.item.core.Item;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TradeRepository extends JpaRepository<Trade, Long> {
    Optional<Trade> findByItem(Item item);

    // 구매내역: buyer_id 기준으로 Trade 엔티티 페이지 조회 (컬렉션 fetch join 금지)
    // to-one 연관은 즉시 로딩하여 N+1 방지
    @EntityGraph(attributePaths = {"item", "item.member"})
    @Query("SELECT t FROM Trade t WHERE t.buyer.id = :buyerId")
    Page<Trade> findByBuyerId(Long buyerId, Pageable pageable);
}
