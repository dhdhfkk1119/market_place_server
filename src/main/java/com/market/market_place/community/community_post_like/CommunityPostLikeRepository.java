package com.market.market_place.community.community_post_like;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CommunityPostLikeRepository extends JpaRepository<CommunityPostLike, Long> {

    CommunityPostLike findByPostIdAndMemberId(Long postId, Long memberId);

}
