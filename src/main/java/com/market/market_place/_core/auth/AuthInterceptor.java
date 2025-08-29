package com.market.market_place._core.auth;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.market.market_place._core._exception.Exception401;
import com.market.market_place._core._exception.Exception500;
import com.market.market_place._core._utils.JwtUtil;
import com.market.market_place.members.domain.Member.MemberRole;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Arrays;

@Slf4j
@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Auth auth = handlerMethod.getMethodAnnotation(Auth.class);

        if (auth == null) {
            return true;
        }

        log.debug("@Auth: JWT 및 권한 검사를 시작합니다.");

        String jwt = request.getHeader(JwtUtil.HEADER);

        if (jwt == null || !jwt.startsWith(JwtUtil.TOKEN_PREFIX)) {
            throw new Exception401("@Auth: 토큰이 없습니다. 로그인이 필요합니다.");
        }

        jwt = jwt.replace(JwtUtil.TOKEN_PREFIX, "");

        try {
            JwtUtil.SessionUser sessionUser = JwtUtil.verifyAndReturnSessionUser(jwt);
            request.setAttribute("sessionUser", sessionUser);

            MemberRole[] allowedRoles = auth.roles();

            if (allowedRoles.length > 0) {
                MemberRole userRole = sessionUser.getRole();
                boolean authorized = Arrays.stream(allowedRoles)
                        .anyMatch(role -> role == userRole);

                if (!authorized) {
                    throw new Exception401("@Auth: 해당 권한이 없습니다.");
                }
            }

            return true;

        } catch (TokenExpiredException e) {
            throw new Exception401("@Auth: 토큰이 만료되었습니다. 다시 로그인해주세요.");
        } catch (JWTDecodeException e) {
            throw new Exception401("@Auth: 유효하지 않은 토큰입니다.");
        } catch (Exception e) {
            log.error("@Auth: 인증 인터셉터 오류", e);
            throw new Exception500("@Auth: 서버 처리 중 오류가 발생했습니다.");
        }
    }
}
