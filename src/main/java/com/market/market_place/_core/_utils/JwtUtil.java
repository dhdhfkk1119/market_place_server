package com.market.market_place._core._utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.market.market_place.members.domain.Member;
import com.market.market_place.members.domain.Role;
import lombok.Builder;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.Date;

// JWT 토큰 생성 및 유효성 검증을 위한 유틸리티 클래스
@Component
public class JwtUtil {

    // JWT 관련 상수
    public static final String HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";

    // === 토큰 만료 시간 재정의 ===
    private static final long ACCESS_TOKEN_EXPIRATION_TIME = 1000L * 60 * 60; // 1시간
    public static final long REFRESH_TOKEN_EXPIRATION_TIME = 1000L * 60 * 60 * 24 * 14; // 14일 (public으로 변경)
    private static final long RESET_TOKEN_EXPIRATION_TIME = 1000L * 60 * 10; // 비밀번호 재설정용 임시 토큰 유효 시간 (10분)

    // 테스트 편의성을 위해 비밀 키를 다시 static 상수로 관리
    private static final String SECRET_KEY = "MySuperSecretKeyForMarketPlaceProject";

    // === 액세스 토큰 생성 ===
    public static String createAccessToken(Member member) {
        Date expiresAt = new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION_TIME);
        return JWT.create()
                .withSubject("access-jwt") // 용도 명확화
                .withExpiresAt(expiresAt)
                .withClaim("id", member.getId())
                .withClaim("loginId", member.getLoginId())
                .withClaim("role", member.getRole().name())
                .sign(Algorithm.HMAC512(SECRET_KEY));
    }

    // === 리프레시 토큰 생성 ===
    public static String createRefreshToken(Member member) {
        Date expiresAt = new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION_TIME);
        return JWT.create()
                .withSubject("refresh-jwt") // 용도 명확화
                .withExpiresAt(expiresAt)
                .withClaim("id", member.getId()) // 리프레시 토큰에는 최소한의 정보만 담는다.
                .sign(Algorithm.HMAC512(SECRET_KEY));
    }

    // JWT 토큰을 검증하고, 디코딩된 JWT 객체를 반환 (범용)
    public static DecodedJWT verify(String jwt) {
        return JWT.require(Algorithm.HMAC512(SECRET_KEY))
                .build()
                .verify(jwt);
    }

    // 액세스 토큰을 검증하고, 사용자 정보를 담은 SessionUser 객체를 반환
    public static SessionUser verifyAndReturnSessionUser(String jwt) throws JWTVerificationException {
        DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC512(SECRET_KEY))
                .withSubject("access-jwt") // 반드시 용도가 access-jwt 인 토큰만 허용
                .build()
                .verify(jwt);

        Long id = decodedJWT.getClaim("id").asLong();
        String loginId = decodedJWT.getClaim("loginId").asString();
        String roleStr = decodedJWT.getClaim("role").asString();

        return SessionUser.builder()
                .id(id)
                .loginId(loginId)
                .role(Role.valueOf(roleStr))
                .build();
    }

    // === 리프레시 토큰에서 memberId 추출 ===
    public static Long getMemberIdFromRefreshToken(String refreshToken) throws JWTVerificationException {
        DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC512(SECRET_KEY))
                .withSubject("refresh-jwt") // 반드시 용도가 refresh-jwt 인 토큰만 허용
                .build()
                .verify(refreshToken);
        return decodedJWT.getClaim("id").asLong();
    }

    // --- 기존 비밀번호 재설정 관련 메서드 ---

    // 비밀번호 재설정용 임시 토큰 생성
    public static String createPasswordResetToken(Member member) {
        Date expiresAt = new Date(System.currentTimeMillis() + RESET_TOKEN_EXPIRATION_TIME);
        return JWT.create()
                .withSubject("password-reset-jwt") // 용도를 명확히 구분
                .withExpiresAt(expiresAt)
                .withClaim("id", member.getId())
                .withClaim("role", member.getRole().name()) // 역할 정보도 포함
                .sign(Algorithm.HMAC512(SECRET_KEY));
    }

    // 비밀번호 재설정용 임시 토큰 검증 및 세션 정보 반환
    public static SessionUser verifyPasswordResetToken(String jwt) throws JWTVerificationException {
        DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC512(SECRET_KEY))
                .withSubject("password-reset-jwt") // 반드시 용도가 일치하는지 확인
                .build()
                .verify(jwt);
        Long id = decodedJWT.getClaim("id").asLong();
        String roleStr = decodedJWT.getClaim("role").asString();
        return SessionUser.builder().id(id).role(Role.valueOf(roleStr)).build();
    }

    // JWT 토큰에서 추출한 사용자 정보를 담는 세션 사용자 객체
    @Getter
    @Builder
    public static class SessionUser {
        private Long id;
        private String loginId;
        private String username;
        private String email;
        private Role role; // 독립된 Role을 사용하도록 수정
    }
}
