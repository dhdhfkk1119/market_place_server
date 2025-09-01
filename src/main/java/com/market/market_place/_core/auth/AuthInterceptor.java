package com.market.market_place._core.auth;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.market.market_place._core._exception.Exception401;
import com.market.market_place._core._exception.Exception403;
import com.market.market_place._core._exception.Exception500;
import com.market.market_place._core._utils.JwtUtil;
import com.market.market_place.members.domain.Member.MemberRole;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler) throws Exception {

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

        jwt = jwt.replace(JwtUtil.TOKEN_PREFIX, "").trim();

        try {
            JwtUtil.SessionUser sessionUser = JwtUtil.verifyAndReturnSessionUser(jwt);
            if (sessionUser == null) {
                throw new Exception401("@Auth: 유효한 토큰이 아닙니다. 다시 로그인해주세요.");
            }
            request.setAttribute("sessionUser", sessionUser);

            // 1. 역할(Role) 검사
            MemberRole[] allowedRoles = auth.roles();
            if (allowedRoles.length > 0) {
                MemberRole userRole = sessionUser.getRole();
                boolean authorized = Arrays.stream(allowedRoles)
                        .anyMatch(role -> role == userRole);

                if (!authorized) {
                    throw new Exception403("@Auth: 해당 리소스에 접근할 권한이 없습니다.");
                }
            }

            // 2. 리소스 소유자(Owner) 확인
            if (auth.isOwner()) {
                // 관리자(ADMIN)는 소유자 확인을 통과
                if (sessionUser.getRole() != MemberRole.ADMIN) {
                    Map<String, String> pathVariables = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
                    String idStr = pathVariables.get("id");

                    if (idStr == null) {
                        log.warn("@Auth(isOwner=true): URL에 {id} 변수가 없어 소유자 확인을 할 수 없습니다.");
                        throw new Exception500("@Auth: 소유자 확인 설정이 잘못되었습니다.");
                    }

                    try {
                        Long resourceId = Long.parseLong(idStr);
                        if (!Objects.equals(sessionUser.getId(), resourceId)) {
                            throw new Exception403("@Auth: 자신의 리소스에만 접근할 수 있습니다.");
                        }
                    } catch (NumberFormatException e) {
                        log.warn("@Auth(isOwner=true): URL의 {id} 값이 유효한 숫자가 아닙니다. ({})", idStr);
                        throw new Exception403("@Auth: 잘못된 요청입니다.");
                    }
                }
            }

            return true;

        } catch (TokenExpiredException e) {
            throw new Exception401("@Auth: 토큰이 만료되었습니다. 다시 로그인해주세요.");
        } catch (JWTDecodeException e) {
            throw new Exception401("@Auth: 유효하지 않은 토큰입니다.");
        } catch (Exception401 | Exception403 e) {
            throw e; // 직접 발생시킨 예외는 그대로 다시 던집니다.
        } catch (Exception e) {
            log.error("@Auth: 인증 인터셉터 오류", e);
            throw new Exception500("@Auth: 서버 처리 중 오류가 발생했습니다.");
        }
    }
}
