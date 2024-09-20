package com.hh.multiboarduserbackend.common.dto.response;

import com.hh.multiboarduserbackend.common.vo.FileVo;
import com.hh.multiboarduserbackend.mappers.FileMapper;
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
