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
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod handlerMethod)) {
            return true;
        }

        Auth auth = handlerMethod.getMethodAnnotation(Auth.class);
        if (auth == null) {
            return true;
        }

        try {
            // 1. 인증: 토큰 검증 및 세션 정보 생성
            JwtUtil.SessionUser sessionUser = verifyTokenAndGetSessionUser(request);

            // 2. 권한: API 접근 역할 검사
            checkRole(auth, sessionUser);

            // 3. 소유권: 리소스 소유자 검사
            checkOwnership(auth, sessionUser, request);

            return true; // 모든 검사 통과

        } catch (TokenExpiredException e) {
            throw new Exception401("@Auth: 토큰이 만료되었습니다. 다시 로그인해주세요.");
        } catch (JWTDecodeException e) {
            throw new Exception401("@Auth: 유효하지 않은 토큰입니다.");
        } catch (Exception401 | Exception403 e) {
            throw e;
        } catch (Exception e) {
            log.error("인증 인터셉터 처리 중 알 수 없는 오류 발생", e);
            throw new Exception500("@Auth: 서버 처리 중 오류가 발생했습니다.");
        }
    }

    // --- Private Helper Methods ---

    // 1. 인증(Authentication) - 토큰을 검증하고 세션 정보를 반환하는 역할
    private JwtUtil.SessionUser verifyTokenAndGetSessionUser(HttpServletRequest request) {
        String jwt = request.getHeader(JwtUtil.HEADER);
        if (jwt == null || !jwt.startsWith(JwtUtil.TOKEN_PREFIX)) {
            throw new Exception401("@Auth: 토큰이 없습니다. 로그인이 필요합니다.");
        }
        jwt = jwt.replace(JwtUtil.TOKEN_PREFIX, "").trim();

        JwtUtil.SessionUser sessionUser = JwtUtil.verifyAndReturnSessionUser(jwt);
        if (sessionUser == null) {
            throw new Exception401("@Auth: 유효한 토큰이 아닙니다. 다시 로그인해주세요.");
        }
        request.setAttribute("sessionUser", sessionUser);
        return sessionUser;
    }

    // 2. 권한(Authorization) - API에 접근 가능한 역할(Role)인지 검사하는 역할
    private void checkRole(Auth auth, JwtUtil.SessionUser sessionUser) {
        if (auth.roles().length > 0) {
            MemberRole userRole = sessionUser.getRole();
            boolean isAuthorized = Arrays.stream(auth.roles())
                    .anyMatch(role -> role == userRole);
            if (!isAuthorized) {
                throw new Exception403("@Auth: 해당 리소스에 접근할 권한이 없습니다.");
            }
        }
    }

    // 3. 소유권(Ownership) - 리소스의 소유자가 맞는지 검사하는 역할
    private void checkOwnership(Auth auth, JwtUtil.SessionUser sessionUser, HttpServletRequest request) {
        if (auth.isOwner()) {
            if (sessionUser.getRole() == MemberRole.ADMIN) {
                return; // 관리자는 소유자 확인을 통과
            }

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
}
