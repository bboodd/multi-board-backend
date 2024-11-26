package com.spring.multiboardbackend.domain.board.docs;

import com.spring.multiboardbackend.domain.board.dto.response.CategoryResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(name = "Category API", description = "카테고리 관련 API")
public interface CategoryControllerDocs {

    @Operation(
            summary = "카테고리 목록 조회",
            description = "게시판 타입별 카테고리 목록을 조회합니다."
    )
    @ApiResponse(
            responseCode = "200",
            description = "조회 성공",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(
                            schema = @Schema(implementation = CategoryResponse.class)
                    )
            )
    )
    @ApiResponse(
            responseCode = "404",
            description = "게시판 타입을 찾을 수 없음",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Response.class)
            )
    )
    ResponseEntity<List<CategoryResponse>> getCategories(
            @Parameter(
                    name = "boardType",
                    description = "게시판 타입 (free, notice, gallery 등)",
                    example = "free",
                    required = true
            ) String boardType
    );
}