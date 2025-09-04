package com.market.market_place.Reply.dto;

import com.market.market_place.Reply.domain.Reply;
import com.market.market_place.members.domain.Member;
import com.market.market_place.qna.domain.Qna;
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
