package com.market.market_place.terms.repository;

import com.market.market_place.terms.domain.MemberAgreedTerms;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberAgreedTermsRepository extends JpaRepository<MemberAgreedTerms, Long> {
}
