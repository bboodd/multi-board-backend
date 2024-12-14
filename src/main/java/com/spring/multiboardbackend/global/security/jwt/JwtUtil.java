package com.spring.multiboardbackend.global.security.jwt;

import com.spring.multiboardbackend.domain.member.enums.Role;
import com.spring.multiboardbackend.global.security.jwt.exception.JwtErrorCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.MacAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    private final JwtProperties jwtProperties;
    private final SecretKey key;
    private final MacAlgorithm alg;

    public JwtUtil(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
        this.alg = Jwts.SIG.HS256;

        var keyBytes = Decoders.BASE64.decode(jwtProperties.secret());
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }


    /**
     * jwt 토큰 생성 메서드
     * @param loginId 로그인 ID
     * @param role 권한
     * @param expired 만료시간
     * @return token
     */
    public String generateToken(String loginId, Role role, long expired) {

        long now = (new Date(System.currentTimeMillis())).getTime();

        // Access Token 생성
        Date expiration = new Date(now + expired);
        return Jwts.builder()
                .subject(loginId)
                .claim("role", role.getRoleName())
                .issuer(jwtProperties.issuer())
                .expiration(expiration)
                .signWith(key, alg)
                .compact();
    }

    /**
     * 토큰 정보 검증 메서드
     * @param token - jwt token
     * @return - boolean
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(key).build()
                    .parseSignedClaims(token);
            return true;
        } catch (ExpiredJwtException e) {
            throw JwtErrorCode.EXPIRED_ACCESS_TOKEN.defaultException(e);
        } catch (JwtException | IllegalArgumentException e) {
            throw JwtErrorCode.WRONG_ACCESS_TOKEN.defaultException(e);
        }
    }

    /**
     * 토큰 받아서 클레임으로 변환하는 메서드
     * @param token - jwt token
     * @return - claims
     */
    public Claims getClaimsFromToken(final String token) {
        try {
            return Jwts.parser()
                    .verifyWith(key).build()
                    .parseSignedClaims(token).getPayload();
        } catch (JwtException | IllegalArgumentException e) {
            throw JwtErrorCode.RESOLVE_TOKEN_ERROR.defaultException(e);
        }

    }

    /**
     * request 받아와서 토큰으로 추출
     * @param request - HttpServletRequest
     * @return - jwt token
     */
    public String getTokenFromHeader(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
            return bearerToken.substring("Bearer ".length());
        }

        return null;
    }
}
