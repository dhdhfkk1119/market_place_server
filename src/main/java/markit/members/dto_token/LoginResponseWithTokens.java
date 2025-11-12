package markit.members.dto_token;

import markit.members.dto_auth.MemberLoginResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponseWithTokens {
    private String accessToken;
    private String refreshToken;
    private Long refreshTokenMaxAgeSeconds;
    private MemberLoginResponse loginResponse;
}
