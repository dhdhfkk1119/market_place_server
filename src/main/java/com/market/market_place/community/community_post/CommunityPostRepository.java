package com.market.market_place.community.community_post;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CommunityPostRepository extends JpaRepository<CommunityPost, Long> {


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


    // 소프트 삭제를 직접 실행하는 쿼리 (더티체킹 대신 사용할 때)
    @Modifying(clearAutomatically = true,flushAutomatically = true)
    @Query("update CommunityPost p set p.deletedAt = :now where p.id = :id")
    int softDelete(@Param("id") Long id, @Param("now")LocalDateTime now);

    // ID로 삭제되지 않은 글만 조회
    @Query("select p from CommunityPost p where p.deletedAt is null")
    List<CommunityPost> findAllActive();

    // memberId로 게시글 목록 조회
    Page<CommunityPost> findByMemberId(Long memberId,Pageable pageable);
}
