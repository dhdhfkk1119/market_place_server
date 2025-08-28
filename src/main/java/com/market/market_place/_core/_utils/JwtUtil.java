package com.market.market_place._core._utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.market.market_place.members.domain.Member;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    // TODO: 이 비밀 키는 외부에 노출되면 안 되며, application.yml 같은 설정 파일로 옮겨야 합니다.
    private static final String SECRET_KEY = "MySuperSecretKeyForMarketPlaceProject";
    private static final long EXPIRATION_TIME = 1000L * 60 * 60 * 24; // 24시간

    /**
     * Member 정보를 담은 JWT 토큰을 생성합니다.
     *
     * @param member 토큰에 담을 회원 정보
     * @return 생성된 JWT 토큰 문자열
     */
    public static String createToken(Member member) {
        Date expiresAt = new Date(System.currentTimeMillis() + EXPIRATION_TIME);
        return JWT.create()
                .withSubject("market-place-jwt")
                .withExpiresAt(expiresAt)
                .withClaim("id", member.getId())
                .withClaim("loginId", member.getLoginId())
                .withClaim("role", member.getRole().name())
                .sign(Algorithm.HMAC512(SECRET_KEY));
    }

    /**
     * JWT 토큰을 검증하고, 디코딩된 JWT 객체를 반환합니다.
     *
     * @param jwt 검증할 토큰 문자열
     * @return 디코딩된 JWT 객체
     */
    public static DecodedJWT verify(String jwt) {
        return JWT.require(Algorithm.HMAC512(SECRET_KEY))
                .build()
                .verify(jwt);
    }
}
