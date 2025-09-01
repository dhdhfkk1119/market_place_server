package com.market.market_place.community.community_comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommunityCommentRepository extends JpaRepository<CommunityComment, Long> {

    // 특정 게시글의 댓글 목록 조회
    List<CommunityComment> findByPostId(Long id);

}
