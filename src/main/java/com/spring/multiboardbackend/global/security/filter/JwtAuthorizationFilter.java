package com.spring.multiboardbackend.global.security.filter;

import com.spring.multiboardbackend.global.security.jwt.JwtUtil;
import com.spring.multiboardbackend.global.security.user.MemberUserDetailsService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j(topic = "JWT 검증 및 인가")
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final MemberUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        try {
            String token = jwtUtil.getTokenFromHeader(request);

            if (StringUtils.hasText(token)) {
                if (!jwtUtil.validateToken(token)) {
                    log.error("Invalid JWT Token");
                    throw new RuntimeException("Invalid JWT Token");  // 예외 던지기
                }

                Claims claims = jwtUtil.getClaimsFromToken(token);
                String loginId = claims.getSubject();

                if (StringUtils.hasText(loginId)) {
                    setAuthentication(request, loginId);
                }
            }
        } catch (Exception e) {
            log.error("JWT 인증 과정에서 오류 발생: {}", e.getMessage());
            SecurityContextHolder.clearContext();
            throw e;  // 예외 재던지기
        }

        chain.doFilter(request, response);
    }

    // 인증 처리
    private void setAuthentication(HttpServletRequest request, String loginId) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(loginId);
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        // 추가적인 세부 정보 설정
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        // 기존 SecurityContext에 인증 설정
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }
}
