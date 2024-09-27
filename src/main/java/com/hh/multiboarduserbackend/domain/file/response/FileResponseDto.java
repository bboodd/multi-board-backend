package com.hh.multiboarduserbackend.domain.file.response;

import lombok.Builder;

@Builder
public record FileResponseDto (
          Long fileId
        , Long postId
        , String originalName
        , String savedName
        , String savedPath
        , long savedSize
        , int deleteYn
) {

}
