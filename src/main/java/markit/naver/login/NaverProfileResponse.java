package markit.naver.login;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter // Lombok을 사용하면 코드가 깔끔해져
@NoArgsConstructor
public class NaverProfileResponse {

    private String resultcode;
    private String message;
    private ProfileData response;

    @Getter
    @NoArgsConstructor
    public static class ProfileData {
        private String id;
        private String email;
        private String nickname;
    }
}
