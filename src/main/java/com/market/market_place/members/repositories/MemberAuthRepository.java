package com.market.market_place.members.repositories;

import com.market.market_place.members.domain.MemberAuth;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberAuthRepository extends JpaRepository<MemberAuth, Long> {
    Optional<MemberAuth> findByEmail(String email);
}
