package com.market.market_place.members.repositories;

import com.market.market_place.members.domain.MemberActivity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberActivityRepository extends JpaRepository<MemberActivity, Long> {
}
