package com.spring.multiboardbackend.domain.post.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "게시글 응답")
@JsonInclude(Include.NON_NULL)
public record PostResponse(
        @Schema(description = "게시글 ID", example = "1")
        Long id,

        @Schema(description = "제목", example = "게시글 제목")
        String title,

        @Schema(description = "내용", example = "게시글 내용")
        String content,

        @Schema(description = "조회수", example = "42")
        int viewCount,

        @Schema(description = "생성일시")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime createdAt,

        @Schema(description = "카테고리명", example = "자유게시판")
        String categoryName,

        @Schema(description = "작성자 닉네임", example = "사용자123")
        String nickname,

        @Schema(description = "첨부파일 수", example = "3")
        int fileCount,

        @Schema(description = "댓글 수", example = "5")
        int commentCount,

        @Schema(description = "썸네일 URL")
        String thumbnailUrl,

        @Schema(description = "비밀글 여부")
        boolean locked,

        @Schema(description = "파일 목록")
        List<FileResponse> files,

        @Schema(description = "댓글 목록")
        List<CommentResponse> comments
) {}
