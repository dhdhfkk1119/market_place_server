package markit.Reply.dto;

import markit.Reply.domain.Reply;
import markit.members.domain.Member;
import markit.qna.domain.Qna;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ReplyRequest {

    private String content;

    public Reply toEntity(Qna qna, Member member) {
        return new Reply(
                this.content,qna,member
        );
    }

}
