package com.market.market_place._core._utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.market.market_place.members.domain.Member;
import com.market.market_place.members.domain.Member.MemberRole;
import lombok.Builder;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.Date;

// JWT 토큰 생성 및 유효성 검증을 위한 유틸리티 클래스
@Component
public class JwtUtil {

    // 테스트 편의성을 위해 비밀 키를 다시 static 상수로 관리
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

    /**
     * JWT 토큰을 검증하고, 사용자 정보를 담은 SessionUser 객체를 반환합니다.
     *
     * @param jwt 검증할 토큰 문자열
     * @return 사용자 정보를 담은 SessionUser 객체
     */
    public static SessionUser verifyAndReturnSessionUser(String jwt) throws JWTVerificationException {
        DecodedJWT decodedJWT = verify(jwt); // static 메서드 호출

        Long id = decodedJWT.getClaim("id").asLong();
        String loginId = decodedJWT.getClaim("loginId").asString();
        String roleStr = decodedJWT.getClaim("role").asString();

        return SessionUser.builder()
                .id(id)
                .loginId(loginId)
                .role(MemberRole.valueOf(roleStr))
                .build();
    }

    /**
     * JWT 토큰에서 추출한 사용자 정보를 담는 세션 사용자 객체
     * 컨트롤러, 서비스 등에서 인증된 사용자 정보를 간편하게 접근할 수 있도록 합니다.
     * 이 클래스는 JWTUtil의 정적 내부 클래스로서 JWTUtil과 함께 관리됩니다.
     */
    @Getter
    @Builder
    public static class SessionUser {
        private Long id;
        private String loginId;
        private String username;
        private String email;
        private MemberRole role;
    }
}
