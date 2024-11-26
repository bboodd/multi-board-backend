package com.spring.multiboardbackend.domain.post.docs;

import com.spring.multiboardbackend.domain.post.dto.request.CommentRequest;
import com.spring.multiboardbackend.domain.post.dto.response.CommentResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "댓글", description = "댓글 API")
public interface CommentControllerDocs {

    @Operation(summary = "댓글 등록", description = "게시글에 댓글을 등록합니다.")
    @ApiResponse(
            responseCode = "201",
            description = "댓글 등록 성공",
            content = @Content(schema = @Schema(implementation = CommentResponse.class))
    )
    @ApiResponse(
            responseCode = "400",
            description = "잘못된 요청"
    )
    @ApiResponse(
            responseCode = "401",
            description = "인증되지 않은 사용자"
    )
    @ApiResponse(
            responseCode = "404",
            description = "게시글을 찾을 수 없음"
    )
    ResponseEntity<CommentResponse> saveComment(
            @Parameter(description = "게시판 타입", example = "free", required = true)
            String boardType,

            @Parameter(description = "게시글 ID", example = "1", required = true)
            Long postId,

            @Parameter(description = "댓글 정보", required = true)
            CommentRequest commentRequest
    );

    @Operation(summary = "댓글 삭제", description = "댓글을 삭제합니다.")
    @ApiResponse(
            responseCode = "200",
            description = "댓글 삭제 성공",
            content = @Content(schema = @Schema(implementation = Boolean.class))
    )
    @ApiResponse(
            responseCode = "401",
            description = "인증되지 않은 사용자"
    )
    @ApiResponse(
            responseCode = "403",
            description = "권한 없음 (작성자가 아닌 경우)"
    )
    @ApiResponse(
            responseCode = "404",
            description = "댓글을 찾을 수 없음"
    )
    ResponseEntity<Boolean> deleteComment(
            @Parameter(description = "게시판 타입", example = "free", required = true)
            String boardType,

            @Parameter(description = "게시글 ID", example = "1", required = true)
            Long postId,

            @Parameter(description = "댓글 ID", example = "1", required = true)
            Long commentId
    );
}