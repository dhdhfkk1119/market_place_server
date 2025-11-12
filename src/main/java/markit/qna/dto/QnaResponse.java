package markit.qna.dto;

import markit.qna.domain.Qna;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QnaResponse {
    private Long id;
    private String question;
    private String memberLoginId;
    private String createdAt;

    public QnaResponse(Qna qna) {
        this.id = qna.getId();
        this.question = qna.getQuestion();
        this.memberLoginId = qna.getMember().getLoginId();
        this.createdAt = qna.getTime();
    }
}
