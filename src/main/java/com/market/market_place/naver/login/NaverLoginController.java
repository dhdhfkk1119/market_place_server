package com.market.market_place.naver.login;

import com.market.market_place.members.dto_token.LoginResponseWithTokens;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/naver")
@RequiredArgsConstructor
public class NaverLoginController {

    private final NaverLoginService naverLoginService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseWithTokens> login(@RequestBody String accessToken) {
        LoginResponseWithTokens loginInfo = naverLoginService.login(accessToken);
        return ResponseEntity.ok().body(loginInfo);
    }
}
