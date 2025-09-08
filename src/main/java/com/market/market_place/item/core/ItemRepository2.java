package com.market.market_place.item.core;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository2 extends JpaRepository<Item, Long>, QuerydslPredicateExecutor<Item> {

}