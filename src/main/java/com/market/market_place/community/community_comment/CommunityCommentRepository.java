package com.market.market_place.community.community_comment;

import com.market.market_place.community.community_post.CommunityPost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommunityCommentRepository extends JpaRepository<CommunityComment, Long> {

    List<CommunityComment> findByPostId(Long id);

    boolean existsByPostAndContent(CommunityPost post, String content);

}
