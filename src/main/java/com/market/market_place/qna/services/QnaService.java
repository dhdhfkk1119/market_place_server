package com.market.market_place.qna.services;

import com.market.market_place._core._exception.Exception403;
import com.market.market_place._core._exception.Exception404;
import com.market.market_place.members.domain.Member;
import com.market.market_place.qna.QnaRepository;
import com.market.market_place.qna.domain.Qna;
import com.market.market_place.qna.dto.QnaRequest;
import com.market.market_place.qna.dto.QnaResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QnaService {

    private final QnaRepository qnaRepository;

    @Transactional
    public QnaResponse createQna(QnaRequest request, Member member) {
        Qna qna = new Qna(request.getQuestion(), member);
        Qna savedQna = qnaRepository.save(qna);
        return new QnaResponse(savedQna);
    }

    @Transactional(readOnly = true)
    public List<QnaResponse> getAllQna() {
        return qnaRepository.findAll()
                .stream()
                .map(QnaResponse::new)
                .collect(Collectors.toList());
    }

    //관리자는 유저의 질문을 수정할수 있지만
    //관리자가 답변을 다는 방식은 PUT(수정을 하는 방식)
    //관리자가 답변을 달 경우 기존 질문이 수정이 되지 않음
    @Transactional
    public QnaResponse updateQna(Long id, QnaRequest request, Long memberId, Member.MemberRole role) {
        Qna qna = qnaRepository.findById(id)
                .orElseThrow(() -> new Exception404("qna를 찾을수 없음"));

        if (role == Member.MemberRole.ADMIN) {
            if (request.getAnswer() != null) {
                qna.setAnswer(request.getAnswer());
            }
        }
        else if (qna.getMember().getId().equals(memberId)) {
            if (request.getQuestion() != null) {
                qna.setQuestion(request.getQuestion());
            }
        }
        else {
            throw new Exception403("qna 수정 권한 없음");
        }
        return new QnaResponse(qna);
    }

    @Transactional
    public void deleteQna(Long id, Long memberId, Member.MemberRole role) {
        Qna qna = qnaRepository.findById(id)
                .orElseThrow(() -> new Exception404("qna를 찾을수 없음"));

        if (!qna.getMember().getId().equals(memberId) && role != Member.MemberRole.ADMIN) {
            throw new Exception403("qna 삭제 권한 없음");
        }
        qnaRepository.deleteById(id);
    }
}