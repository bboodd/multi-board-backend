package com.spring.multiboardbackend.global.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import java.io.IOException;

@RequiredArgsConstructor
@Component
@Slf4j
public class AdminAuthInterceptor implements HandlerInterceptor {

    private static final String LOGIN_PAGE = "/admin/login";

    /**
     * 컨트롤러로 들어오기전 어드민 인증 인터셉터 메서드
     * @param request - request
     * @param response - response
     * @param handler - handler
     * @return - boolean
     * @throws IOException - redirect 시 발생할 수 있는 예외
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        String requestURI = request.getRequestURI();

        // 로그인 페이지는 인증 체크 제외
        if (requestURI.equals(LOGIN_PAGE)) {
            return true;
        }

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("ADMIN_ID") == null) {
            // 로그인되지 않은 경우 로그인 페이지로 리다이렉트
            response.sendRedirect(LOGIN_PAGE + "?redirectUrl=" + requestURI);
            return false;
        }

        return true;
    }
}