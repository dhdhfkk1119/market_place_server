package com.market.market_place.notice.dto;

import com.market.market_place.notice.Notice;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NoticeRequest {
    private String title;
    private String content;

    public Notice toEntity() {
        return new Notice(title, content);
    }

}
