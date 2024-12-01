package com.spring.multiboardbackend.domain.post.docs;

import com.spring.multiboardbackend.global.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

@Tag(name = "파일", description = "파일 API")
public interface FileControllerDocs {

    @Operation(
            summary = "파일 다운로드",
            description = "게시글의 첨부파일을 다운로드합니다."
    )
    @ApiResponse(
            responseCode = "200",
            description = "파일 다운로드 성공",
            content = @Content(
                    mediaType = "application/octet-stream",
                    schema = @Schema(type = "string", format = "binary")
            )
    )
    @ApiResponse(
            responseCode = "404",
            description = "파일을 찾을 수 없음",
            content = @Content(
                    schema = @Schema(implementation = ErrorResponse.class))
    )
    @ApiResponse(
            responseCode = "500",
            description = "파일 다운로드 중 서버 오류 발생",
            content = @Content(
                    schema = @Schema(implementation = ErrorResponse.class))
    )
    ResponseEntity<Resource> downloadFile(

            @Parameter(description = "파일 ID", example = "1", required = true)
            Long fileId
    );
}
