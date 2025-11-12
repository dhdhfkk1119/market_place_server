package markit.notice.dto;

import markit.notice.Notice;
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
