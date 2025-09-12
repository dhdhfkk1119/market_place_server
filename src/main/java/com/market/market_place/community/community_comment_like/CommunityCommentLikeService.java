package com.market.market_place.community.community_comment_like;

import com.market.market_place._core._exception.Exception404;
import com.market.market_place._core._utils.JwtUtil;
import com.market.market_place.community.community_comment.CommunityComment;
import com.market.market_place.community.community_comment.CommunityCommentRepository;
import com.market.market_place.community.community_post_like.CommunityPostLikeResponse;
import com.market.market_place.members.domain.Member;
import com.market.market_place.members.repositories.MemberRepository;
import com.market.market_place.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class CommunityCommentLikeService {

    private final CommunityCommentLikeRepository likeRepository;
    private final CommunityCommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final NotificationService notificationService;

    @Transactional
    public CommunityCommentLikeResponse.ResponseDTO toggleLike(Long commentId, JwtUtil.SessionUser sessionUser){

        CommunityComment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new Exception404("댓글을 찾을 수 없습니다"));

        Member member = memberRepository.findById(sessionUser.getId())
                .orElseThrow(() -> new Exception404("사용자를 찾을 수 없습니다"));

        Optional<CommunityCommentLike> commentLike = likeRepository.findByCommentIdAndMemberId(commentId, sessionUser.getId());

        boolean liked;
        if(commentLike.isPresent()){
            likeRepository.delete(commentLike.get());
            comment.updateLikeCount(comment.getLikeCount() -1); // 좋아요 취소 -1
            liked = false;
        } else {
            CommunityCommentLike like = CommunityCommentLike.builder()
                    .comment(comment)
                    .member(member)
                    .build();
            likeRepository.save(like);
            comment.updateLikeCount(comment.getLikeCount() + 1); // 좋아요 등록
            liked = true;
        }
        notificationService.sendCommentLike(comment.getMember().getId().toString(), comment.getContent());
        return new CommunityCommentLikeResponse.ResponseDTO(liked, (long)comment.getLikeCount());
    }

    // 좋아요 수 조회
    public Long getLikeCount(Long commentId){
        return commentRepository.findById(commentId)
                .map(comment -> (long)comment.getLikeCount())
                .orElse(0L);
    }

}
