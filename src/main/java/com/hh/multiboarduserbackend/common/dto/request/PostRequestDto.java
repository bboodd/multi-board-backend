package com.hh.multiboarduserbackend.common.dto.request;

import io.jsonwebtoken.lang.Collections;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Builder
public record PostRequestDto(
          Long postId
        , @NotNull(message = "분류를 선택해 주세요.")
          Long categoryId
        , @NotBlank(message = "제목을 입력해 주세요.")
          @Size(max = 100, message = "제목은 100자 이하여야합니다.")
          String title
        , @NotBlank(message = "내용을 입력해 주세요.")
          @Size(max = 4000, message = "내용은 4000자 이하여야합니다.")
          String content
        , List<MultipartFile> files
        , List<Long> removeFileIds
        , int lockYn
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
