package com.market.market_place.community.community_post;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CommunityPostRepository extends JpaRepository<CommunityPost, Long>{


    // 전체조회 페이징처리
    @Query("SELECT DISTINCT p FROM CommunityPost p JOIN FETCH p.topic LEFT JOIN FETCH p.comments")
    Page<CommunityPost> findAllWithTopicAndComments(Pageable pageable);


    // 댓글과 사용자 한번에 조회
    @Query("SELECT p FROM CommunityPost p LEFT JOIN FETCH p.comments c LEFT JOIN FETCH c.member WHERE p.id = :postId")
    Optional<CommunityPost> findByIdWithComments(@Param("postId") Long postId);

    // 검색기능
    @Query("SELECT DISTINCT p FROM CommunityPost p " +
            "LEFT JOIN p.topic t " +
            "WHERE (:keyword IS NULL OR p.title LIKE %:keyword% OR p.content LIKE %:keyword%) " +
            "AND (:categories IS NULL OR t.name IN :categories)")
    Page<CommunityPost> search(@Param("keyword") String keyword,
                               @Param("categories") List<String> categories,
                               Pageable pageable);

}
