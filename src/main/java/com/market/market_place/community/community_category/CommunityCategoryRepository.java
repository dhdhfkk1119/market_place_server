package com.market.market_place.community.community_category;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommunityCategoryRepository extends JpaRepository<CommunityCategory, Long> {

    Optional<CommunityCategory> findByName(String name);

}
