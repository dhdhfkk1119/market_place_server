package com.market.market_place.terms.repository;

import com.market.market_place.terms.domain.Terms;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TermsRepository extends JpaRepository<Terms, Long> {
}
