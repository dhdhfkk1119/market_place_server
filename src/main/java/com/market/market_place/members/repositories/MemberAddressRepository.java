package com.market.market_place.members.repositories;

import com.market.market_place.members.domain.MemberAddress;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberAddressRepository extends JpaRepository<MemberAddress, Long> {
}
