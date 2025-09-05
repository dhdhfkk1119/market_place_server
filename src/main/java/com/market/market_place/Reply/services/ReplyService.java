package com.market.market_place.Reply.services;

import com.market.market_place.Reply.domain.Reply;
import com.market.market_place.Reply.domain.ReplyRepository;
import com.market.market_place.Reply.dto.ReplyRequest;
import com.market.market_place.Reply.dto.ReplyResponse;
import com.market.market_place._core._exception.Exception403;
import com.market.market_place._core._exception.Exception404;
import com.market.market_place.members.domain.Member;
import com.market.market_place.members.domain.Role;
import com.market.market_place.members.repositories.MemberRepository;
import com.market.market_place.qna.QnaRepository;
import com.market.market_place.qna.domain.Qna;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReplyService {

    private final ReplyRepository replyRepository;
    private final QnaRepository qnaRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public ReplyResponse createReply(Long qnaId, Long memberId, ReplyRequest request) {
        Qna qna = qnaRepository.findById(qnaId)
                .orElseThrow(() -> new Exception404("QNA를 찾을 수 없습니다"));

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new Exception404("회원을 찾을수 없습니다"));

        if (qna.getMember().getId().equals(memberId) || member.getRole() == Role.ADMIN) {
            Reply reply = request.toEntity(qna, member);
            Reply savedReply = replyRepository.save(reply);

            return new ReplyResponse(savedReply, member.getLoginId(), member.getRole().name());
        } else {
            throw new Exception403("권한이 없습니다");
        }
    }

    @Transactional
    public void deleteReply(Long replyId, Long memberId, Role role) {
        Reply reply = replyRepository.findById(replyId)
                .orElseThrow(() -> new Exception404("답변을 찾을 수 없습니다"));

        if(!reply.getMember().getId().equals(memberId) && role != Role.ADMIN) {
            throw new Exception403("권한이 없습니다");
        }
        replyRepository.delete(reply);
    }

    @Transactional
    public ReplyResponse updateReply(Long replyId, ReplyRequest request, Long memberId) {
        Reply reply = replyRepository.findById(replyId)
                .orElseThrow(() -> new Exception404("답변을 찾을 수 없습니다."));

        Member sessionMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new Exception404("회원을 찾을 수 없습니다."));

        if (!reply.getMember().getId().equals(sessionMember.getId()) && sessionMember.getRole() != Role.ADMIN) {
            throw new Exception403("수정할 권한이 없습니다.");
        }

        reply.setContent(request.getContent());
        Reply updatedReply = replyRepository.save(reply);
        return new ReplyResponse(updatedReply);
    }

    public ReplyResponse getReplyById(Long replyId) {
        Reply reply = replyRepository.findById(replyId)
                .orElseThrow(() -> new Exception404("답변을 찾을 수 없습니다."));
        return new ReplyResponse(reply);
    }

    public Page<ReplyResponse> getRepliesByQnaId(Long qnaId, Pageable pageable) {
        Page<Reply> replyPage = replyRepository.findAllByQna_Id(qnaId, pageable);
        return replyPage.map(ReplyResponse::new);
    }

}
