package com.market.market_place.community.community_post;

import com.market.market_place.community.community_comment.CommunityComment;
import com.market.market_place.community.community_post_image.CommunityPostImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommunityPostRepository extends JpaRepository<CommunityPost, Long> {

    // 이미지 조회
    @Query("SELECT i FROM CommunityPostImage i WHERE i.post.id = :id")
    List<CommunityPostImage> findByIdWithImages(@Param("id") Long id);

    // 댓글 조회
    @Query("SELECT c FROM CommunityComment c WHERE c.post.id = :id")
    List<CommunityComment> findByIdWithComments(@Param("id") Long id);

}
