package com.spring.multiboardbackend.domain.post.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Schema(description = "게시글 요청")
public record PostRequest(

        @Schema(description = "카테고리 ID", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "카테고리를 선택해주세요")
        Long categoryId,

        @Schema(description = "제목", example = "게시글 제목", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "제목을 입력해주세요")
        @Size(max = 100, message = "제목은 100자 이하여야 합니다")
        String title,

        @Schema(description = "내용", example = "게시글 내용", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "내용을 입력해주세요")
        @Size(max = 4000, message = "내용은 4000자 이하여야 합니다")
        String content,

        @Schema(description = "첨부 파일 목록")
        List<MultipartFile> files,

        @Schema(description = "삭제할 파일 ID 목록")
        List<Long> removeFileIds,

        @Schema(description = "비밀글 여부", defaultValue = "false")
        Boolean locked
) {
    @Builder
    public PostRequest {
        files = files != null ? files : new ArrayList<>();
        removeFileIds = removeFileIds != null ? removeFileIds : new ArrayList<>();
    }
}