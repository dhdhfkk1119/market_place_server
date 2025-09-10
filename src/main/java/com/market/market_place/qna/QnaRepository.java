package com.market.market_place.qna;

import com.market.market_place.qna.domain.Qna;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface QnaRepository extends JpaRepository<Qna, Long> {

    Page<Qna> findAll(Pageable pageable);
    Page<Qna> findByMemberId(Long memberId, Pageable pageable);

}
