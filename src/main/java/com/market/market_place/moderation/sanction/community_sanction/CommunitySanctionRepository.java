package com.market.market_place.moderation.sanction.community_sanction;// package com.market.market_place.community.sanction;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CommunitySanctionRepository extends JpaRepository<CommunitySanction, Long> {

    Optional<CommunitySanction> findFirstByMember_IdOrderByIdDesc(Long memberId);

    boolean existsByReport_Id(Long reportId);
    Optional<CommunitySanction> findByReport_Id(Long reportId);

    List<CommunitySanction> findByMember_IdAndActiveTrueAndEndAtAfter(Long memberId, LocalDateTime now);

    List<CommunitySanction> findByActiveTrueAndEndAtBefore(LocalDateTime now);

    boolean existsByMember_IdAndActiveTrueAndEndAtAfter(Long memberId, LocalDateTime now);

}
