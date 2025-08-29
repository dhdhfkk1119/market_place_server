package com.market.market_place.community.community_post_image;

import com.market.market_place.community.community_post.CommunityPost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommunityPostImageRepository extends JpaRepository<CommunityPostImage, Long> {

    Optional<CommunityPostImage> findByPostAndImageUrl(CommunityPost post, String imageUrl);
}
