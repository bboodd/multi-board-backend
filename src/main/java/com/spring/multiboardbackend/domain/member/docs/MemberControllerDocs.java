package com.spring.multiboardbackend.domain.member.docs;

import com.spring.multiboardbackend.domain.member.dto.response.MemberResponse;
import com.spring.multiboardbackend.global.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "회원", description = "회원 API")
@SecurityRequirement(name = "bearer-key")
public interface MemberControllerDocs {

    @Operation(
            summary = "내 정보 조회",
            description = "로그인한 회원의 정보를 조회합니다."
    )
    @ApiResponse(
            responseCode = "200",
            description = "조회 성공",
            content = @Content(
                    schema = @Schema(implementation = MemberResponse.class)
            )
    )
    @ApiResponse(
            responseCode = "401",
            description = "인증되지 않은 사용자",
            content = @Content(
                    schema = @Schema(implementation = ErrorResponse.class))
    )
    @ApiResponse(
            responseCode = "404",
            description = "회원 정보를 찾을 수 없음",
            content = @Content(
                    schema = @Schema(implementation = ErrorResponse.class))
    )
    ResponseEntity<MemberResponse> getMyInfo();
}