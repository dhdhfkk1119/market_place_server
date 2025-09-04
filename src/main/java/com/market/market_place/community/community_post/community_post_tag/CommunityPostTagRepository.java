package com.market.market_place.community.community_post.community_post_tag;

import com.market.market_place.community.community_post.CommunityPost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommunityPostTagRepository extends JpaRepository<CommunityPostTag, Long> {
    void deleteByPost(CommunityPost post);
}
