package com.spring.multiboardbackend.domain.member.dto.request;

import com.spring.multiboardbackend.domain.member.exception.MemberErrorCode;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Schema(description = "회원가입 요청")
public record SignUpRequest(
        @Schema(description = "로그인 아이디", example = "user123")
        @NotBlank(message = "아이디를 입력해 주세요")
        @Pattern(regexp = "^[a-zA-Z0-9_-]{4,12}$", message = "아이디는 4~12자의 영문, 숫자, 특수문자(-,_)만 사용 가능합니다")
        String loginId,

        @Schema(description = "비밀번호", example = "Pass123!@#")
        @NotBlank(message = "비밀번호를 입력해 주세요")
        @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[#?!@$ %^&*-]).{4,16}$", message = "비밀번호는 4~16자의 영문, 숫자, 특수문자를 모두 포함해야 합니다")
        String password,

        @Schema(description = "비밀번호 확인", example = "Pass123!@#")
        @NotBlank(message = "비밀번호 확인을 입력해 주세요")
        @Pattern(regexp = "^(?!.*([a-zA-Z0-9#?!@$ %^&*-])\\1{2}).*$", message = "동일한 문자를 3번 이상 연속해서 사용할 수 없습니다")
        String checkPassword,

        @Schema(description = "닉네임", example = "사용자123")
        @NotBlank(message = "닉네임을 입력해 주세요")
        @Pattern(regexp = "^[a-zA-Z0-9가-힣]{2,10}$", message = "닉네임은 2~10자의 한글, 영문, 숫자만 사용 가능합니다")
        String nickname
) {

    /**
     * 비밀번호 유효성 검증
     */
    public SignUpRequest {
        validatePasswords(password, checkPassword, loginId);
    }

    /**
     * 비밀번호 관련 모든 검증을 처리하는 private 메서드
     */
    private static void validatePasswords(String password, String checkPassword, String loginId) {
        if (password == null || checkPassword == null) {
            throw MemberErrorCode.SIGN_UP_PASSWORD_CHECK_ERROR.defaultException();
        }

        if (!password.equals(checkPassword)) {
            throw MemberErrorCode.SIGN_UP_PASSWORD_CHECK_ERROR.defaultException();
        }

        if (loginId != null && loginId.equals(password)) {
            throw MemberErrorCode.ID_PASSWORD_EQUALS_ERROR.defaultException();
        }
    }

    /**
     * 보안을 위한 toString 재정의
     */
    @Override
    public String toString() {
        return String.format(
                "SignUpRequest(loginId=%s, password=****, checkPassword=****, nickname=%s)",
                loginId, nickname
        );
    }

}
