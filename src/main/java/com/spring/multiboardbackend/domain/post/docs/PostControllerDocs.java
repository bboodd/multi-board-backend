package com.spring.multiboardbackend.domain.post.docs;

import com.spring.multiboardbackend.domain.post.dto.request.PostRequest;
import com.spring.multiboardbackend.domain.post.dto.response.PostResponse;
import com.spring.multiboardbackend.domain.post.dto.response.PostsResponse;
import com.spring.multiboardbackend.global.common.request.SearchRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "게시글", description = "게시글 API")
public interface PostControllerDocs {

    @Operation(summary = "게시글 목록 조회", description = "게시판 타입에 따른 게시글 목록을 조회합니다.")
    @ApiResponse(
            responseCode = "200",
            description = "조회 성공",
            content = @Content(schema = @Schema(implementation = PostsResponse.class))
    )
    @ApiResponse(
            responseCode = "400",
            description = "잘못된 게시판 타입"
    )
    ResponseEntity<PostsResponse> getPosts(
            @Parameter(description = "게시판 타입", example = "free", required = true)
            String boardType,

            @Parameter(description = "검색 조건")
            SearchRequest request
    );

    @Operation(summary = "게시글 상세 조회", description = "게시글의 상세 정보를 조회합니다.")
    @ApiResponse(
            responseCode = "200",
            description = "조회 성공",
            content = @Content(schema = @Schema(implementation = PostResponse.class))
    )
    @ApiResponse(
            responseCode = "404",
            description = "게시글을 찾을 수 없음"
    )
    ResponseEntity<PostResponse> getPost(
            @Parameter(description = "게시판 타입", example = "free", required = true)
            String boardType,

            @Parameter(description = "게시글 ID", example = "1", required = true)
            Long postId
    );

    @Operation(summary = "게시글 작성", description = "새로운 게시글을 작성합니다.")
    @ApiResponse(
            responseCode = "201",
            description = "작성 성공",
            content = @Content(schema = @Schema(implementation = PostResponse.class))
    )
    @ApiResponse(
            responseCode = "400",
            description = "잘못된 요청"
    )
    @ApiResponse(
            responseCode = "401",
            description = "인증되지 않은 사용자"
    )
    ResponseEntity<PostResponse> savePost(
            @Parameter(description = "게시판 타입", example = "free", required = true)
            String boardType,

            @Parameter(description = "게시글 정보", required = true)
            PostRequest request
    );

    @Operation(summary = "게시글 수정", description = "기존 게시글을 수정합니다.")
    @ApiResponse(
            responseCode = "200",
            description = "수정 성공",
            content = @Content(schema = @Schema(implementation = PostResponse.class))
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
            responseCode = "403",
            description = "권한 없음 (작성자가 아닌 경우)"
    )
    @ApiResponse(
            responseCode = "404",
            description = "게시글을 찾을 수 없음"
    )
    ResponseEntity<PostResponse> updatePost(
            @Parameter(description = "게시판 타입", example = "free", required = true)
            String boardType,

            @Parameter(description = "게시글 ID", example = "1", required = true)
            Long postId,

            @Parameter(description = "수정할 게시글 정보", required = true)
            PostRequest request
    );

    @Operation(summary = "게시글 삭제", description = "게시글을 삭제합니다.")
    @ApiResponse(
            responseCode = "200",
            description = "삭제 성공",
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
            description = "게시글을 찾을 수 없음"
    )
    ResponseEntity<Boolean> deletePost(
            @Parameter(description = "게시판 타입", example = "free", required = true)
            String boardType,

            @Parameter(description = "게시글 ID", example = "1", required = true)
            Long postId
    );
}