package com.spring.multiboardbackend.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.multiboardbackend.domain.member.enums.Role;
import com.spring.multiboardbackend.global.exception.CustomException;
import com.spring.multiboardbackend.global.security.jwt.JwtProperties;
import com.spring.multiboardbackend.global.security.jwt.JwtUtil;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Base64;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JwtUtilTest {

    private JwtUtil jwtUtil;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final String SECRET_KEY = "serectserectserectserectserectserectserectserectserectserect";
    private static final String ISSUER = "com.spring";
    private static final String TEST_LOGIN_ID = "testUser";
    private static final Role TEST_ROLE = Role.USER; // 필요한 Role로 설정
    private static final long ACCESS_TOKEN_TIME = 1000L * 60 * 60 * 24 * 30; // 한 달 (30일)

    @BeforeEach
    void setUp() {
        JwtProperties jwtProperties = new JwtProperties(SECRET_KEY, ISSUER);
        jwtUtil = new JwtUtil(jwtProperties);
    }

    @Nested
    @DisplayName("토큰 생성 테스트")
    class GenerateToken {

        @Test
        @DisplayName("토큰 생성 성공")
        void generateToken_Success() {
            // given
            String loginId = TEST_LOGIN_ID;
            Role role = TEST_ROLE;
            long expired = ACCESS_TOKEN_TIME;

            // when
            String token = jwtUtil.generateToken(loginId, role, expired);
            Claims claims = jwtUtil.getClaimsFromToken(token);

            String extractedLoginId = claims.getSubject();
            String extractedRole = claims.get("role", String.class);

            // then
            assertThat(extractedLoginId).isEqualTo(loginId);
            assertThat(extractedRole).isEqualTo(role.getRoleName());
            assertThat(claims.getIssuer()).isEqualTo(ISSUER);
            assertThat(claims.getExpiration()).isAfter(new Date(System.currentTimeMillis()));
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
            JwtUtil differentProvider = new JwtUtil(differentProperties);

            String token = differentProvider.generateToken(TEST_LOGIN_ID, TEST_ROLE, ACCESS_TOKEN_TIME);

            // then
            assertThrows(CustomException.class, () -> jwtUtil.getClaimsFromToken(token));
        }

        @Test
        @DisplayName("손상된 토큰 검증 실패")
        void validateToken_DamagedToken_Fail() {
            // given
            String token = jwtUtil.generateToken(TEST_LOGIN_ID, TEST_ROLE, ACCESS_TOKEN_TIME);
            String damagedToken = createDamagedToken(token);

            // then
            assertThrows(CustomException.class, () -> jwtUtil.getClaimsFromToken(damagedToken));
        }

        @Test
        @DisplayName("변조된 Payload 토큰 검증 실패")
        void validateToken_ModifiedPayload_Fail() throws IOException {
            // given
            String token = jwtUtil.generateToken(TEST_LOGIN_ID, TEST_ROLE, ACCESS_TOKEN_TIME);
            String modifiedToken = modifyTokenPayload(token, "newLoginId", "ADMIN");

            // then
            assertThrows(CustomException.class, () -> jwtUtil.getClaimsFromToken(modifiedToken));
        }

        @Test
        @DisplayName("null 토큰 검증 실패")
        void validateToken_NullToken_Fail() {
            assertThrows(CustomException.class, () -> jwtUtil.getClaimsFromToken(null));
        }

        @Test
        @DisplayName("유효한 토큰 검증 성공")
        void validateToken_ValidToken_Success() {
            // given
            String token = jwtUtil.generateToken(TEST_LOGIN_ID, TEST_ROLE, ACCESS_TOKEN_TIME);

            // when
            boolean isValid = jwtUtil.validateToken(token);

            // then
            assertThat(isValid).isTrue();
        }
    }

    /**
     * 토큰의 일부를 변경하여 손상된 토큰을 생성하는 메서드
     * @param token 원본 토큰
     * @return 손상된 토큰
     */
    private String createDamagedToken(String token) {
        char[] tokenChar = token.toCharArray();
        tokenChar[0] = 'a';
        tokenChar[1] = 'b';
        tokenChar[2] = 'c';
        tokenChar[3] = 'd';
        tokenChar[4] = 'e';
        return String.valueOf(tokenChar);
    }

    /**
     * 토큰의 Payload를 수정하여 변조된 토큰을 생성하는 메서드
     * @param token 원본 토큰
     * @param newLoginId 새로운 로그인 ID
     * @param newRole 새로운 역할
     * @return 변조된 토큰
     * @throws IOException JSON 파싱/생성 예외
     */
    private String modifyTokenPayload(String token, String newLoginId, String newRole) throws IOException {
        String[] split = token.split("\\.");
        if (split.length != 3) {
            throw new IllegalArgumentException("Invalid JWT token format");
        }

        // Decode the payload
        byte[] decodedPayload = Base64.getUrlDecoder().decode(split[1]);
        String payloadJson = new String(decodedPayload);

        // Parse the payload into TokenPayload object
        TokenPayload tokenPayload = objectMapper.readValue(payloadJson, TokenPayload.class);

        // Modify the payload
        tokenPayload.sub = newLoginId;
        tokenPayload.role = newRole;

        // Serialize the modified payload back to JSON
        String modifiedPayloadJson = objectMapper.writeValueAsString(tokenPayload);

        // Encode the modified payload
        String modifiedPayload = Base64.getUrlEncoder().withoutPadding().encodeToString(modifiedPayloadJson.getBytes());

        // Reconstruct the token with modified payload
        return String.join(".", split[0], modifiedPayload, split[2]);
    }

    /**
     * 토큰 Payload를 나타내는 내부 클래스
     */
    private static class TokenPayload {
        public String iss;
        public Date exp;
        public String sub;   // Subject (loginId)
        public String role;  // Role
    }
}
