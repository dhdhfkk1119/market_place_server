package com.market.market_place.community.community_comment;

import com.market.market_place._core._exception.Exception403;
import com.market.market_place._core._exception.Exception404;
import com.market.market_place._core._utils.JwtUtil;
import com.market.market_place.community.community_post.CommunityPost;
import com.market.market_place.community.community_post.CommunityPostRepository;
import com.market.market_place.members.domain.Member;
import com.market.market_place.members.repositories.MemberRepository;
import com.market.market_place.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class CommunityCommentService {

    private final CommunityCommentRepository commentRepository;
    private final CommunityPostRepository postRepository;
    private final MemberRepository memberRepository;
    private final NotificationService notificationService;

    // 저장
    @Transactional
    public CommunityCommentResponse.ResponseDTO save(Long postId, CommunityCommentRequest.SaveDTO saveDTO,
                                                     JwtUtil.SessionUser sessionUser) {

        CommunityPost post = postRepository.findById(postId)
                .orElseThrow(() -> new Exception404("해당 ID의 게시글을 찾을 수 없습니다"));

        Member member = memberRepository.findById(sessionUser.getId())
                .orElseThrow(() -> new Exception404("사용자를 찾을 수 없습니다"));

        CommunityComment comment = saveDTO.toEntity(member, post);
        commentRepository.save(comment);
        notificationService.sendComment(post.getMember().getId().toString(), post.getTitle());
        return new CommunityCommentResponse.ResponseDTO(comment);
    }

    // 조회
    public List<CommunityCommentResponse.ResponseDTO> findWithPost(Long postId){
        return commentRepository.findByPostId(postId).stream()
                .map(CommunityCommentResponse.ResponseDTO::new)
                .collect(Collectors.toList());
    }

    // 수정
    @Transactional
    public CommunityCommentResponse.ResponseDTO update(Long id, CommunityCommentRequest.UpdateDTO updateDTO,
                                   JwtUtil.SessionUser sessionUser){
        CommunityComment comment = commentRepository.findById(id)
                .orElseThrow(() -> new Exception404("해당 댓글이 없습니다"));
        if(!comment.isOwner(sessionUser.getId())){
            throw new Exception403("본인이 작성한 댓글만 수정할 수 있습니다");
        }
        comment.update(updateDTO);
        return new CommunityCommentResponse.ResponseDTO(comment);
    }


    // 삭제
    @Transactional
    public void delete(Long id, JwtUtil.SessionUser sessionUser) {
        CommunityComment comment = commentRepository.findById(id)
                .orElseThrow(() -> new Exception404("삭제할 댓글이 없습니다"));

        if(!comment.isOwner(sessionUser.getId())){
            throw  new Exception403("본인이 작성한 댓글만 삭제할 수 있습니다");
        }
        commentRepository.delete(comment);
    }
}
