package markit.naver.login;

import markit._core._exception.Exception401;
import markit.members.domain.Provider;
import markit.members.dto_auth.SocialLoginRequest;
import markit.members.dto_token.LoginResponseWithTokens;
import markit.members.services.MemberAuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
@RequiredArgsConstructor
public class NaverLoginService {

    private final RestTemplate restTemplate;
    private final MemberAuthService memberAuthService;

    @Value("${naver.api.login.token-validate-url}")
    private String naverProfileApiUrl;

    public LoginResponseWithTokens login(String accessToken) {

        NaverProfileResponse.ProfileData profile = getNaverProfile(accessToken);

        SocialLoginRequest socialLoginRequest = SocialLoginRequest.builder()
                .provider(Provider.NAVER)
                .providerId(profile.getId())
                .email(profile.getEmail())
                .build();

        return memberAuthService.socialLogin(socialLoginRequest);
    }

    private NaverProfileResponse.ProfileData getNaverProfile(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity<String> entity = new HttpEntity<>("", headers);

        try {
            ResponseEntity<NaverProfileResponse> response = restTemplate.exchange(
                    naverProfileApiUrl,
                    HttpMethod.GET,
                    entity,
                    NaverProfileResponse.class
            );
            return response.getBody().getResponse();

        } catch (HttpClientErrorException e) {
            log.error("Naver API call failed: {}", e.getMessage()); // log 추가
            throw new Exception401("네이버 인증 정보가 유효하지 않습니다.");
        }
    }
}
