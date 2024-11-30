package com.spring.multiboardbackend.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.multiboardbackend.global.exception.CustomException;
import com.spring.multiboardbackend.global.security.jwt.JwtProperties;
import com.spring.multiboardbackend.global.security.jwt.JwtProvider;
import com.spring.multiboardbackend.global.security.jwt.JwtToken;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JwtProviderTest {

    private JwtProvider jwtProvider;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final String SECRET_KEY = "serectserectserectserectserectserectserectserectserectserect";
    private static final String ISSUER = "com.spring";
    private static final Long TEST_MEMBER_ID = 1L;

    @BeforeEach
    void setUp() {
        JwtProperties jwtProperties = new JwtProperties(SECRET_KEY, ISSUER);
        jwtProvider = new JwtProvider(jwtProperties, Jwts.SIG.HS256);
    }

    @Nested
    @DisplayName("토큰 생성 테스트")
    class GenerateToken {

        @Test
        @DisplayName("토큰 생성 성공")
        void generateToken_Success() {
            // given

            // when
            JwtToken token = jwtProvider.generateToken(TEST_MEMBER_ID);
            Long verifyMemberId = jwtProvider.getAuthentication(token.accessToken());

            // then
            assertThat(verifyMemberId).isEqualTo(TEST_MEMBER_ID);
        }
    }

    @Nested
    @DisplayName("토큰 검증 테스트")
    class ValidateToken {

        @Test
        @DisplayName("다른 SecretKey로 생성된 토큰 검증 실패")
        void validateToken_DifferentSecretKey_Fail() {
            // given
            JwtProperties differentProperties = new JwtProperties(
                    "aaaaaaaaaaaaabbbbbbbbbbbbbcccccccdddddddeeeee",
                    ISSUER
            );
            JwtProvider differentProvider = new JwtProvider(differentProperties, Jwts.SIG.HS256);

            // when
            JwtToken token = differentProvider.generateToken(TEST_MEMBER_ID);

            // then
            String invalidToken = token.accessToken();
            assertThrows(CustomException.class, () -> jwtProvider.getAuthentication(invalidToken));
        }

        @Test
        @DisplayName("손상된 토큰 검증 실패")
        void validateToken_DamagedToken_Fail() {
            // given
            JwtToken token = jwtProvider.generateToken(TEST_MEMBER_ID);
            String damagedToken = createDamagedToken(token.accessToken());

            // then
            assertThrows(CustomException.class, () ->
                    jwtProvider.getAuthentication(damagedToken)
            );
        }

        @Test
        @DisplayName("변조된 Payload 토큰 검증 실패")
        void validateToken_ModifiedPayload_Fail() throws IOException {
            // given
            JwtToken token = jwtProvider.generateToken(TEST_MEMBER_ID);
            String modifiedToken = modifyTokenPayload(token.accessToken(), 1234L);

            // then
            assertThrows(CustomException.class, () ->
                    jwtProvider.getAuthentication(modifiedToken)
            );
        }

        @Test
        @DisplayName("null 토큰 검증 실패")
        void validateToken_NullToken_Fail() {
            assertThrows(CustomException.class, () ->
                    jwtProvider.getAuthentication(null)
            );
        }

        @Test
        @DisplayName("유효한 토큰 검증 성공")
        void validateToken_ValidToken_Success() {
            // given
            JwtToken token = jwtProvider.generateToken(21L);

            // when
            boolean isValid = jwtProvider.validateToken(token.accessToken());

            // then
            assertThat(isValid).isTrue();
        }
    }

    private String createDamagedToken(String token) {
        char[] tokenChar = token.toCharArray();
        tokenChar[0] = 'a';
        tokenChar[1] = 'b';
        tokenChar[2] = 'c';
        tokenChar[3] = 'd';
        tokenChar[4] = 'e';
        return String.valueOf(tokenChar);
    }

    private String modifyTokenPayload(String token, Long newMemberId) throws IOException {
        String[] split = token.split("\\.");
        byte[] decodePayload = Base64.getDecoder().decode(split[1]);

        TokenPayload tokenPayload = objectMapper.readValue(decodePayload, TokenPayload.class);
        tokenPayload.auth = newMemberId;
        byte[] payloadJson = objectMapper.writeValueAsBytes(tokenPayload);
        String modifiedPayload = Base64.getEncoder().encodeToString(payloadJson);

        return String.join(".", split[0], modifiedPayload, split[2]);
    }

    private static class TokenPayload {
        public String iss;
        public Long exp;
        public Long auth;
    }
}