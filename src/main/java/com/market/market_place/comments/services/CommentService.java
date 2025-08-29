package com.market.market_place.comments.services;

import com.market.market_place._core._exception.Exception403;
import com.market.market_place._core._exception.Exception404;
import com.market.market_place.comments.domain.Comment;
import com.market.market_place.comments.domain.CommentRepository;
import com.market.market_place.comments.dto.CommentRequest;
import com.market.market_place.comments.dto.CommentResponse;
import com.market.market_place.members.domain.Member;
import com.market.market_place.notice.Notice;
import com.market.market_place.notice.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final NoticeRepository noticeRepository;

    @Transactional
    public CommentResponse createComment(Long noticeId, CommentRequest request, Member member) {
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new Exception404("해당 공지사항을 찾을수 없습니다"));

        Comment comment = new Comment(request.getContent(), notice, member);
        Comment savedComment = commentRepository.save(comment);
        return CommentResponse.fromEntity(savedComment);
    } // QNA가 제대로 적혔는가 판독, 오류로 의해 제대로 안 적혔을시 404

    @Transactional
    public CommentResponse updateComment(Long id, CommentRequest request, Long memberId, Member.MemberRole role) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new Exception404("해당 댓글을 찾을수 없습니다"));

        if (!comment.getMember().getId().equals(memberId) && role != Member.MemberRole.ADMIN) {
            throw new Exception403("댓글을 수정할 권한이 없습니다");
        } // 본인의 댓글이거나 관리자여야만 수정 가능

        comment.setContent(request.getContent());
        Comment updatedComment = commentRepository.save(comment);
        return CommentResponse.fromEntity(updatedComment);
    }

    @Transactional
    public void deleteComment(Long id, Long memberId, Member.MemberRole role) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new Exception404("해당 댓글을 찾을수 없습니다"));

        if (!comment.getMember().getId().equals(memberId) && role != Member.MemberRole.ADMIN) {
            throw new Exception403("댓글을 삭제할 권한이 없습니다");
        } // 본인의 댓글이거나 관리자여야만 삭제 가능

        commentRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<CommentResponse> getCommentsByNoticeId(Long noticeId) {
        return commentRepository.findAllByNoticeIdOrderByCreatedAtDesc(noticeId)
                .stream()
                .map(CommentResponse::fromEntity)
                .collect(Collectors.toList());
    }
}