package com.market.market_place.moderation.sanction.community_sanction;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommunitySanctionRepository extends JpaRepository<CommunitySanction,Long> {
}
