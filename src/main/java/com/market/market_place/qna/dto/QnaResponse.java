package com.market.market_place.qna.dto;

import com.market.market_place.qna.domain.Qna;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class QnaResponse {
    private Long id;
    private String question;
    private String answer;
    private String memberLoginId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public QnaResponse(Qna qna) {
        this.id = qna.getId();
        this.question = qna.getQuestion();
        this.answer = qna.getAnswer();
        this.memberLoginId = qna.getMember().getLoginId();
        this.createdAt = qna.getCreatedAt();
        this.updatedAt = qna.getUpdatedAt();
    }
}
