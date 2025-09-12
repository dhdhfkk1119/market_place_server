package com.market.market_place.moderation.sanction.item_sanction;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ItemSanctionRepository extends JpaRepository<ItemSanction,Long> {

    List<ItemSanction> findByActiveTrueAndEndAtBefore(LocalDateTime now);

    Optional<ItemSanction> findFirstByMember_IdOrderByIdDesc(Long memberId);
}
