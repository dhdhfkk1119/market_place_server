package markit.qna.services;

import markit.Reply.domain.Reply;
import markit.Reply.domain.ReplyRepository;
import markit.Reply.dto.ReplyResponse;
import markit._core._exception.Exception403;
import markit._core._exception.Exception404;
import markit.members.domain.Member;
import markit.members.domain.Role;
import markit.qna.QnaRepository;
import markit.qna.domain.Qna;
import markit.qna.dto.QnaAndRepliesResponse;
import markit.qna.dto.QnaRequest;
import markit.qna.dto.QnaResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QnaService {

    private final QnaRepository qnaRepository;
    private final ReplyRepository replyRepository;

    public QnaResponse createQna(QnaRequest request, Member member) {
        Qna qna = new Qna(request.getQuestion(), member);
        Qna savedQna = qnaRepository.save(qna);
        return new QnaResponse(savedQna);
    }

    @Transactional
    public QnaResponse updateQna(Long id, QnaRequest request, Long memberId, Role role) {
        Qna qna = qnaRepository.findById(id)
                .orElseThrow(() -> new Exception404("qna를 찾을수 없음"));

        if (qna.getMember().getId().equals(memberId) || role == Role.ADMIN) {
            if (request.getQuestion() != null) {
                qna.setQuestion(request.getQuestion());
            }
        } else {
            throw new Exception403("Q&A 수정 권한 없음");
        }
        return new QnaResponse(qna);
    }

    @Transactional
    public void deleteQna(Long id, Long memberId, Role role) {
        Qna qna = qnaRepository.findById(id)
                .orElseThrow(() -> new Exception404("qna를 찾을수 없음"));

        if (!qna.getMember().getId().equals(memberId) && role != Role.ADMIN) {
            throw new Exception403("Q&A 삭제 권한 없음");
        }
        qnaRepository.deleteById(id);
    }

    public QnaAndRepliesResponse getQnaWithReplies(Long qnaId) {
        Qna qna = qnaRepository.findById(qnaId)
                .orElseThrow(() -> new Exception404("Q&A를 찾을 수 없습니다."));
        return new QnaAndRepliesResponse(qna);
    }

    public Page<QnaResponse> getQnas(Pageable pageable) {
        Page<Qna> qnaPage = qnaRepository.findAll(pageable);
        return qnaPage.map(QnaResponse::new);
    }

    public Page<ReplyResponse> getRepliesByQnaId(Long qnaId, Pageable pageable) {
        Page<Reply> replyPage = replyRepository.findAllByQna_Id(qnaId, pageable);
        return replyPage.map(ReplyResponse::new);
    }

}