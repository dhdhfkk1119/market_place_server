package com.market.market_place.members.repositories;

import com.market.market_place.members.domain.Member;
import com.market.market_place.members.domain.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    // Member 객체로 리프레시 토큰을 찾는 메소드
    Optional<RefreshToken> findByMember(Member member);

    // Member ID로 리프레시 토큰을 삭제하는 메소드 (필요 시 사용)
    void deleteByMember(Member member);
}
