package com.market.market_place.item.item_favorite;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ItemFavoriteRepository extends JpaRepository<ItemFavorite,Long> {
    Optional<ItemFavorite> findByMemberIdAndItemId(Long memberId,Long itemId);
    Long countByItemId(Long itemId);
}
