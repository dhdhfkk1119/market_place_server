package markit.email.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FindIdResponse {
    private final String maskedLoginId;
}
