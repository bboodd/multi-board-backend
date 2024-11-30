package com.spring.multiboardbackend.domain.member.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "중복 검사 요청 DTO")
public record DuplicateCheckRequest(
        @Schema(description = "검사할 값(아이디/닉네임)", example = "user123")
        @NotBlank(message = "값을 입력해 주세요.")
        @Size(min = 2, max = 20, message = "2자 이상 20자 이하로 입력해 주세요.")
        String value
) {
        /**
         * 정적 메서드
         */
        public static DuplicateCheckRequest of(String value) {
                return new DuplicateCheckRequest(value);
        }
}
