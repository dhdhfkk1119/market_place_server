package com.market.market_place.community.community_comment_like;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommunityCommentLikeRepository extends JpaRepository<CommunityCommentLike, Long> {

    Optional<CommunityCommentLike> findByCommentIdAndMemberId(Long commentId, Long memberId);
}
