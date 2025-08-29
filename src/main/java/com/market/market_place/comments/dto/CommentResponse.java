package com.market.market_place.comments.dto;

import com.market.market_place.comments.domain.Comment;
import com.market.market_place.members.dtos.MemberLoginResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class CommentResponse {

    private Long id;
    private String content;
    private LocalDateTime createdAt;
    private Long noticeId;
    private MemberLoginResponse member;

    public static CommentResponse fromEntity(Comment comment) {
        return CommentResponse.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .noticeId(comment.getNotice().getId())
                .member(new MemberLoginResponse(comment.getMember()))
                .build();
    }
}
