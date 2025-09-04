package com.market.market_place.Reply.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply,Long> {

    List<Reply> findAllByQna_IdOrderByCreatedAtAsc(Long qnaId);

}
