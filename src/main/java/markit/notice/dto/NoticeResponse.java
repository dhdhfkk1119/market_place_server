package markit.notice.dto;

import markit.notice.Notice;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class NoticeResponse {

    private Long id;
    private String title;
    private String content;
    private String createdAt;

    public static NoticeResponse fromEntity(Notice notice) {
        return new NoticeResponse(
                notice.getId(),
                notice.getTitle(),
                notice.getContent(),
                notice.getTime()
        );
    }
}
