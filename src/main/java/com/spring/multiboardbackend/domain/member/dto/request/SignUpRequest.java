package com.spring.multiboardbackend.domain.member.dto.request;

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
        @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[#?!@$ %^&*-]).{4,16}$", message = "비밀번호는 4~16자의 영문, 숫자, 특수문자를 모두 포함해야 합니다")
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
     * 보안을 위한 toString 재정의
     */
    @Override
    public String toString() {
        return String.format(
                "SignUpRequest(loginId=%s, password=****, checkPassword=****, nickname=%s)",
                loginId, nickname
        );
    }

    /**
     * 정적 메서드
     */
    public static SignUpRequest of(String loginId, String password, String checkPassword, String nickname) {
        return new SignUpRequest(loginId, password, checkPassword, nickname);
    }


}
