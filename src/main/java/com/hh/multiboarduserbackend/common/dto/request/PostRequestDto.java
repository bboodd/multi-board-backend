package com.hh.multiboarduserbackend.common.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Builder
public record PostRequestDto(
          Long postId
        , @NotBlank(message = "분류를 선택해 주세요.")
          Long categoryId
        , @NotBlank(message = "제목을 입력해 주세요.")
          @Size(max = 100, message = "제목은 100자 이하여야합니다.")
          String title
        , @NotBlank(message = "내용을 입력해 주세요.")
          @Size(max = 4000, message = "내용은 4000자 이하여야합니다.")
          String content
        , @Size(max = 5, message = "파일은 최대 5개까지 업로드 가능합니다.")
          List<MultipartFile> files
        , List<Long> removeFileIds
) {

    public PostRequestDto {
        if(Objects.isNull(files)) {
            files = new ArrayList<>();
        }
        if(Objects.isNull(removeFileIds)) {
            removeFileIds = new ArrayList<>();
        }
    }
}
