package com.market.market_place.qna.dto;

import com.market.market_place.Reply.dto.ReplyResponse;
import com.market.market_place.qna.domain.Qna;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class QnaAndRepliesResponse {

    private Long id;
    private String question;
    private String status;
    private String authorLoginId;
    private LocalDateTime createdAt;
    private List<ReplyResponse> replies;

    public QnaAndRepliesResponse(Qna qna) {
        this.id = qna.getId();
        this.question = qna.getQuestion();
        this.status = qna.getStatus();
        this.authorLoginId = qna.getMember().getLoginId();
        this.createdAt = qna.getCreatedAt();
        this.replies = qna.getReplies().stream()
                .map(ReplyResponse::new)
                .toList();
    }

}
