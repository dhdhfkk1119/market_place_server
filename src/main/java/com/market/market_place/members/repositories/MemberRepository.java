package com.market.market_place.members.repositories;

import com.market.market_place.members.domain.Member;
import com.market.market_place.members.domain.Provider;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    // 로그인 ID로 회원 조회 (고유 값)
    Optional<Member> findByLoginId(String loginId);

    // 이메일로 회원 조회 (고유 값)
    Optional<Member> findByEmail(String email);

    // 소셜 로그인 정보(Provider, ProviderId)로 회원 조회
    Optional<Member> findByProviderAndProviderId(Provider provider, String providerId);

    // 로그인 ID 존재 여부 확인 (회원가입 시 중복 체크용)
    boolean existsByLoginId(String loginId);

    // 이메일 존재 여부 확인 (회원가입 시 중복 체크용)
    boolean existsByEmail(String email);
}
