package com.spring.multiboardbackend.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.multiboardbackend.domain.member.service.MemberService;
import com.spring.multiboardbackend.global.exception.CustomException;
import com.spring.multiboardbackend.global.security.jwt.JwtProperties;
import com.spring.multiboardbackend.global.security.jwt.JwtProvider;
import com.spring.multiboardbackend.global.security.jwt.JwtToken;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class JwtProviderTest {

    private JwtProvider jwtProvider;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setUp() {
        final JwtProperties jwtProperties = new JwtProperties(
                "serectserectserectserectserectserectserectserectserectserect",
                "com.hh"
        );

        jwtProvider = new JwtProvider(jwtProperties, Jwts.SIG.HS256);
    }

    private MemberService memberService;

    @Test
    void 토큰생성테스트_성공() {
        Long memberId = 1L;

        final JwtToken token = jwtProvider.generateToken(memberId);
        final Long verifyMemberId = jwtProvider.getAuthentication(token.accessToken());

        assertThat(verifyMemberId).isEqualTo(memberId);

    }

    @Test
    void 토큰검증테스트_다른SecretKey_실패() {
        Long memberId = 123L;
        final JwtProperties 또다른토큰생성기 = new JwtProperties(
                "aaaaaaaaaaaaabbbbbbbbbbbbbcccccccdddddddeeeee",
                "com.hh"
        );
        final JwtToken token = new JwtProvider(또다른토큰생성기, Jwts.SIG.HS256).generateToken(memberId);

        assertThrows(CustomException.class, () ->
                jwtProvider.getAuthentication(token.accessToken())
        );
    }

    @Test
    void 토큰검증테스트_손상된토큰_실패() {
        Long memberId = 1L;

        JwtToken token = jwtProvider.generateToken(memberId);
        final char[] tokenChar = token.accessToken().toCharArray();
        tokenChar[0] = 'a';
        tokenChar[1] = 'b';
        tokenChar[2] = 'c';
        tokenChar[3] = 'd';
        tokenChar[4] = 'e';
        final String tokenWithNoise = String.valueOf(tokenChar);

        assertThrows(CustomException.class, () ->
                jwtProvider.getAuthentication(tokenWithNoise)
        );
    }

    @Test
    void 토큰검증테스트_변조된토큰_실패() throws IOException {
        Long memberId = 1L;
        JwtToken token = jwtProvider.generateToken(memberId);

        final String modulationToken = 토큰_Payload_변조(token.accessToken(), 1234L);

        assertThrows(CustomException.class, () ->
                jwtProvider.getAuthentication(modulationToken)
        );
    }

    private String 토큰_Payload_변조(String token, Long newMemberId) throws IOException {
        final String[] split = token.split("\\.");
        final byte[] decodePayload = Base64.getDecoder().decode(split[1]);

        // 변조
        final TokenPayload tokenPayload = objectMapper.readValue(decodePayload, TokenPayload.class);
        tokenPayload.auth = newMemberId;
        final byte[] payloadJson = objectMapper.writeValueAsBytes(tokenPayload);
        final String modulationPayload = Base64.getEncoder().encodeToString(payloadJson);

        final String modulationToken = String.join(".", split[0], modulationPayload, split[2]);
        return modulationToken;
    }

    public static class TokenPayload {
        public String iss;
        public Long exp;
        public Long auth;
    }

//    @Test
//    void 토큰검증테스트_유효기간만료_실패() {
//        Long memberId = 123L;
//        final JwtProperties 만료토큰생성기 = new JwtProperties(
//                "secretsecretsecretsecretsecretsecretsecretsecretsecretsecret",
//                "com.hh",
//                -100
//        );
//        final JwtToken token = new JwtProvider(만료토큰생성기, Jwts.SIG.HS256).generateToken(memberId);
//
//        assertThrows(CustomException.class, () ->
//                jwtProvider.getAuthentication(token.accessToken())
//        );
//    }

    @Test
    void 토큰검증테스트_null_실패() {
        String accessToken = null;

        assertThrows(CustomException.class, () ->
                jwtProvider.getAuthentication(accessToken)
        );
    }

    @Test
    void 토큰유효성검증_성공() {
        Long memberId = 21L;

        final JwtToken token = jwtProvider.generateToken(memberId);

        String accessToken = "eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJjb20uaGgiLCJleHAiOjE3MjQ5NDUwNTgsImF1dGgiOjIxfQ.u3Rreky52SR-kcRYJvd6yRfJ1sHYb18A_G35znTP42k";

        assertThat(jwtProvider.validateToken(token.accessToken())).isEqualTo(true);
    }

}