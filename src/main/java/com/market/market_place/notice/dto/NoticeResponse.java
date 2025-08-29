package com.market.market_place.notice.dto;

import com.market.market_place.notice.Notice;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class NoticeResponse {

    private Long id;
    private String title;
    private String content;
    private LocalDateTime createdAt;

    public static NoticeResponse fromEntity(Notice notice) {
        return new NoticeResponse(
                notice.getId(),
                notice.getTitle(),
                notice.getContent(),
                notice.getCreatedAt()
        );
    }
}
