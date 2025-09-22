package com.market.market_place.community.community_post_like;

import com.market.market_place.community.community_post.CommunityPost;
import com.market.market_place.item.core.Item;
import com.market.market_place.members.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CommunityPostLikeRepository extends JpaRepository<CommunityPostLike, Long> {

    Optional<CommunityPostLike> findByPostIdAndMemberId(Long postId, Long memberId);

    boolean existsByPostAndMember(CommunityPost post, Member member);




}
