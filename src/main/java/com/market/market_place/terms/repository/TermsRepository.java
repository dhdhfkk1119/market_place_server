package com.market.market_place.terms.repository;

import com.market.market_place.terms.domain.Terms;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TermsRepository extends JpaRepository<Terms, Long> {

    // 필수 약관(isRequired가 true인 약관) 목록을 조회하는 쿼리 메소드
    List<Terms> findAllByIsRequired(boolean isRequired);
}
