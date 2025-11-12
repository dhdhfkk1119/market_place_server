package markit.members.dto_token;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TokenReissueResponse {
    private String newAccessToken;
    private String newRefreshToken;
    private Long refreshTokenMaxAgeSeconds;
}
