package com.market.market_place.Reply.dto;

import com.market.market_place.Reply.domain.Reply;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ReplyResponse {

    private Long id;
    private String content;
    private String authorLoginId;
    private String authorRole;
    private LocalDateTime createdAt;

    public ReplyResponse(Reply reply) {
        this.id = reply.getId();
        this.content = reply.getContent();
        this.authorRole = reply.getMember().getRole().name();
        this.authorLoginId = reply.getMember().getLoginId();
        this.createdAt = reply.getCreatedAt();
    }

    public ReplyResponse(Reply reply, String authorLoginId, String authorRole) {
        this.id = reply.getId();
        this.content = reply.getContent();
        this.createdAt = reply.getCreatedAt();
        this.authorLoginId = authorLoginId;
        this.authorRole = authorRole;
    }

}
