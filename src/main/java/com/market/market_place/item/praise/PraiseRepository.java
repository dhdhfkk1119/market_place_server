package com.market.market_place.item.praise;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PraiseRepository extends JpaRepository<Praise, Long>, QuerydslPredicateExecutor<Praise> {

}
