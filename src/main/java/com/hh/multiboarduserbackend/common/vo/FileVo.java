package com.hh.multiboarduserbackend.common.vo;

import com.hh.multiboarduserbackend.common.dto.request.FileRequestDto;
import com.hh.multiboarduserbackend.mappers.FileMapper;
import lombok.Builder;

@Builder
public record FileVo(
          Long fileId
        , Long postId
        , String originalName
        , String savedName
        , String savedPath
        , long savedSize
        , int deleteYn
) {

    public static FileVo toVo(FileRequestDto fileRequestDto) {
        return FileMapper.INSTANCE.toVo(fileRequestDto);
    }
}
