package markit.qna.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QnaRequest {
    private String question;
    private String answer;
}
