package com.spring.multiboardbackend.global.interceptor;

import com.spring.multiboardbackend.global.security.auth.AuthenticationContextHolder;
import com.spring.multiboardbackend.domain.member.annotation.LoginMember;
import com.spring.multiboardbackend.global.security.jwt.JwtProvider;
import com.spring.multiboardbackend.global.security.jwt.exception.JwtErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

@RequiredArgsConstructor
@Component
public class AuthInterceptor implements HandlerInterceptor {

    private final JwtProvider jwtProvider;

    /**
     * 컨트롤러로 들어오기전 인증 인터셉터 메서드
     * @param request - request
     * @param response - response
     * @param handler - handler
     * @return - boolean
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if(!isAuthMethod(handler)) {
            return true;
        }

        final String token = resolveToken(request);

        if(token != null && jwtProvider.validateToken(token)) {
            // 토큰이 유효할 경우
            final Long memberId = jwtProvider.getAuthentication(token);
            AuthenticationContextHolder.setContext(memberId);
            return true;
        }

        return false;
    }

    /**
     * 컨트롤러 수행후 ThreadLocal clear 해주는 함수
     * @param request - request
     * @param response - response
     * @param handler - handler
     * @param modelAndView - modelAndView
     * @throws Exception - exception
     */
    @Override
    public void postHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler,
                           final ModelAndView modelAndView) throws Exception {
        AuthenticationContextHolder.clear();
    }

    /**
     * LoginMember 어노테이션이 붙었는지 확인하는 메서드
     * @param handler - method
     * @return - boolean
     */
    private boolean isAuthMethod(final Object handler) {
        // 정적 리소스 제외(view 관련 파일 등)
        if (handler instanceof ResourceHttpRequestHandler) {
            return false;
        }
    
        final HandlerMethod handlerMethod = (HandlerMethod) handler;
        return handlerMethod.hasMethodAnnotation(LoginMember.class);
    }

    /**
     * request 받아와서 토큰으로 추출
     * @param request - HttpServletRequest
     * @return - jwt token
     */
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken == null) {
            throw JwtErrorCode.REQUIRED_ACCESS_TOKEN.defaultException();
        }

        if(StringUtils.hasText(bearerToken) || bearerToken.startsWith("Bearer")) {
            return bearerToken.substring("Bearer ".length());
        }

        return null;
    }
}
