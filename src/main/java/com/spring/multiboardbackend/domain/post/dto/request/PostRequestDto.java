package com.spring.multiboardbackend.domain.post.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Builder
public record PostRequestDto(
          Long postId
        , Long categoryId
        , @NotBlank(message = "제목을 입력해 주세요.")
          @Size(max = 100, message = "제목은 100자 이하여야합니다.")
          String title
        , @NotBlank(message = "내용을 입력해 주세요.")
          @Size(max = 4000, message = "내용은 4000자 이하여야합니다.")
          String content
        , List<MultipartFile> files
        , List<Long> removeFileIds
        , Boolean lockYn
) {

    public PostRequestDto {
        if(CollectionUtils.isEmpty(files)) {
            files = new ArrayList<>();
        }
        if(CollectionUtils.isEmpty(removeFileIds)) {
            removeFileIds = new ArrayList<>();
        }
    }
}
