package com.market.market_place.community.community_post_like;

import com.market.market_place._core._exception.Exception404;
import com.market.market_place._core._utils.JwtUtil;
import com.market.market_place.community.community_post.CommunityPost;
import com.market.market_place.community.community_post.CommunityPostRepository;
import com.market.market_place.members.domain.Member;
import com.market.market_place.members.repositories.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class CommunityPostLikeService {

    private final CommunityPostLikeRepository likeRepository;
    private final CommunityPostRepository postRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public CommunityPostLikeResponse.ResponseDTO toggleLike(Long postId, JwtUtil.SessionUser sessionUser){

        CommunityPost post = postRepository.findById(postId)
                .orElseThrow(() -> new Exception404("게시글을 찾을 수 없습니다"));

        Member member = memberRepository.findById(sessionUser.getId())
                .orElseThrow(() -> new Exception404("사용자를 찾을 수 없습니다"));

        CommunityPostLike postLike = likeRepository.findByPostIdAndMemberId(postId, sessionUser.getId());

        boolean liked;
        if(postLike != null){
            likeRepository.delete(postLike);
            post.updateLikeCount(post.getLikeCount() -1); // 좋아요 -1
            liked = false;
        } else {
            CommunityPostLike like = CommunityPostLike.builder()
                    .post(post)
                    .member(member)
                    .build();
            likeRepository.save(like);
            post.updateLikeCount(post.getLikeCount() + 1); // 좋아요 +1
            liked = true;
        }

        return new CommunityPostLikeResponse.ResponseDTO(liked, (long) post.getLikeCount());
    }

    // 좋아요 수 조회
    public Long getLikeCount(Long postId){
        return postRepository.findById(postId)
                .map(post -> (long)post.getLikeCount())
                .orElse(0L);
    }
}
