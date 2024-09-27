package com.hh.multiboarduserbackend.domain.file.request;

import lombok.Builder;

@Builder
public record FileRequestDto(
            Long fileId
          , Long postId
          , String originalName
          , String savedName
          , String savedPath
          , long savedSize
) {
}
