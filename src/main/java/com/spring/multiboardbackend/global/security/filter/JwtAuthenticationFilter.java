package com.spring.multiboardbackend.global.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.multiboardbackend.domain.member.enums.Role;
import com.spring.multiboardbackend.global.security.jwt.JwtToken;
import com.spring.multiboardbackend.global.security.jwt.JwtUtil;
import com.spring.multiboardbackend.global.security.user.MemberUserDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j(topic = "로그인 및 JWT 생성")
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper;

    private static final String LOGIN_ID_PARAM = "loginId";
    private static final long ACCESS_TOKEN_TIME = 1000L * 60 * 60 * 24 * 30; // 한달

    public JwtAuthenticationFilter(JwtUtil jwtUtil, ObjectMapper objectMapper) {
        this.jwtUtil = jwtUtil;
        this.objectMapper = objectMapper;
        setFilterProcessesUrl("/api/auth/login");
    }

    // 로그인 시, username 과 password 를 바탕으로 UsernamePasswordAuthenticationToken 을 발급
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if (!request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }
        String loginId = obtainLoginId(request);
        loginId = (loginId != null) ? loginId.trim() : "";
        String password = obtainPassword(request);
        password = (password != null) ? password : "";
        UsernamePasswordAuthenticationToken authRequest = UsernamePasswordAuthenticationToken.unauthenticated(loginId,
                password);
        // Allow subclasses to set the "details" property
        setDetails(request, authRequest);
        return getAuthenticationManager().authenticate(authRequest);
    }

    protected String obtainLoginId(HttpServletRequest request) {
        return request.getParameter(LOGIN_ID_PARAM);
    }

    // 로그인 성공 시
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        MemberUserDetails userDetails = (MemberUserDetails) authResult.getPrincipal();
        String loginId = userDetails.getUsername();
        Role role = Role.fromId(userDetails.getMember().getRoleId());
        log.info("Authentication successful for user: {}", loginId);

        String token = jwtUtil.generateToken(loginId, role, ACCESS_TOKEN_TIME);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);

        JwtToken jwtToken = JwtToken.builder()
                .grantType("Bearer")
                .accessToken(token)
                .refreshToken(null)
                .build();

        String jsonResponse = objectMapper.writeValueAsString(jwtToken);
        response.getWriter().write(jsonResponse);
        response.getWriter().flush();
    }

    // 로그인 실패 시
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        log.warn("Authentication failed: {}", failed.getMessage());

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "아이디 혹은 비밀번호가 일치하지 않습니다.");

        String jsonResponse = objectMapper.writeValueAsString(errorResponse);
        response.getWriter().write(jsonResponse);
        response.getWriter().flush();
    }
}
