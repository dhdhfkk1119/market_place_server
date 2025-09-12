package com.market.market_place.item.core;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long>, ItemRepositoryCustom,QuerydslPredicateExecutor<Item> {

    @Query("SELECT DISTINCT i FROM Item i JOIN FETCH i.member m LEFT JOIN i.itemCategory c " +
            "WHERE (:keyword IS NULL OR i.title LIKE %:keyword% OR i.content LIKE %:keyword%) " +
            "AND (:tags IS NULL OR c.name IN :tags) ORDER BY i.createdAt DESC")
    List<Item> search(@Param("keyword") String keyword, @Param("tags") List<String> tags);


}
