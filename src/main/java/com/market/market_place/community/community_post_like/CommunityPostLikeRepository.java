package com.market.market_place.community.community_post_like;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommunityPostLikeRepository extends JpaRepository<CommunityPostLike, Long> {

    Optional<CommunityPostLike> findByPostIdAndMemberId(Long postId, Long memberId);

}
