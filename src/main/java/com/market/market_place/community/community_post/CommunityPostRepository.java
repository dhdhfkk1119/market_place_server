package com.market.market_place.community.community_post;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CommunityPostRepository extends JpaRepository<CommunityPost, Long> {


    // 전체조회 페이징처리
    @Query("SELECT p FROM CommunityPost p JOIN FETCH p.topic")
    Page<CommunityPost> findAllWithTopic(Pageable pageable);
}
