package com.spring.multiboardbackend.domain.member.docs;

import com.spring.multiboardbackend.domain.member.dto.request.DuplicateCheckRequest;
import com.spring.multiboardbackend.domain.member.dto.request.LoginRequest;
import com.spring.multiboardbackend.domain.member.dto.request.SignUpRequest;
import com.spring.multiboardbackend.domain.member.dto.response.LoginResponse;
import com.spring.multiboardbackend.domain.member.dto.response.MemberResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "인증", description = "회원가입/로그인 API")
public interface AuthControllerDocs {

    @Operation(summary = "회원가입", description = "새로운 회원을 등록합니다.")
    @ApiResponse(
            responseCode = "201",
            description = "회원가입 성공",
            content = @Content(schema = @Schema(implementation = MemberResponse.class))
    )
    @ApiResponse(
            responseCode = "400",
            description = "잘못된 요청 (유효성 검사 실패)"
    )
    @ApiResponse(
            responseCode = "409",
            description = "중복된 아이디 또는 닉네임"
    )
    ResponseEntity<MemberResponse> signUp(
            @Schema(description = "회원가입 정보", requiredMode = Schema.RequiredMode.REQUIRED)
            SignUpRequest request
    );

    @Operation(summary = "로그인", description = "로그인을 수행하고 JWT 토큰을 발급받습니다.")
    @ApiResponse(
            responseCode = "200",
            description = "로그인 성공",
            content = @Content(schema = @Schema(implementation = LoginResponse.class))
    )
    @ApiResponse(
            responseCode = "400",
            description = "잘못된 요청 (유효성 검사 실패)"
    )
    @ApiResponse(
            responseCode = "401",
            description = "로그인 실패 (아이디 또는 비밀번호 불일치)"
    )
    ResponseEntity<LoginResponse> login(
            @Schema(description = "로그인 정보", requiredMode = Schema.RequiredMode.REQUIRED)
            LoginRequest request
    );

    @Operation(summary = "로그인 아이디 중복 확인", description = "회원가입 시 로그인 아이디 중복을 확인합니다.")
    @ApiResponse(
            responseCode = "200",
            description = "중복 확인 완료",
            content = @Content(schema = @Schema(implementation = Boolean.class))
    )
    @ApiResponse(
            responseCode = "400",
            description = "잘못된 요청 (유효성 검사 실패)"
    )
    ResponseEntity<Boolean> checkDuplicateLoginId(
            @Schema(description = "중복 확인할 로그인 아이디", requiredMode = Schema.RequiredMode.REQUIRED)
            DuplicateCheckRequest request
    );

    @Operation(summary = "닉네임 중복 확인", description = "회원가입 시 닉네임 중복을 확인합니다.")
    @ApiResponse(
            responseCode = "200",
            description = "중복 확인 완료",
            content = @Content(schema = @Schema(implementation = Boolean.class))
    )
    @ApiResponse(
            responseCode = "400",
            description = "잘못된 요청 (유효성 검사 실패)"
    )
    ResponseEntity<Boolean> checkDuplicateNickname(
            @Schema(description = "중복 확인할 닉네임", requiredMode = Schema.RequiredMode.REQUIRED)
            DuplicateCheckRequest request
    );
}