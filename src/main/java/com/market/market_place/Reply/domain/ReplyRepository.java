package com.market.market_place.Reply.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReplyRepository extends JpaRepository<Reply, Long> {

    Page<Reply> findAllByQna_Id(Long qnaId, Pageable pageable);

}
