package com.market.market_place.item.core;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository2 extends JpaRepository<Item, Long> {
    // 나눔 상품 (가격이 0원인 상품) 조회
    List<Item> findByPrice(Long price);

    // 5천원 이하, 만원이하, 2만원이하 상품 조회
    // price 값이 지정된 값(maxPrice)보다 작거나 같은 모든 상품 반환
    List<Item> findByPriceLessThanEqual(Long price);
}
