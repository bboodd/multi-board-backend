package com.hh.multiboarduserbackend.mappers;

import com.hh.multiboarduserbackend.common.dto.request.FileRequestDto;
import com.hh.multiboarduserbackend.common.dto.response.FileResponseDto;
import com.hh.multiboarduserbackend.common.vo.FileVo;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FileMapper {

    FileMapper INSTANCE = Mappers.getMapper(FileMapper.class);

    FileResponseDto toDto(FileVo fileVo);

    FileVo toVo(FileRequestDto fileRequestDto, Long postId);

    default List<FileVo> toVoList(List<FileRequestDto> fileList, Long postId) {
        List<FileVo> result = new ArrayList<>();
        fileList.forEach(fileRequestDto -> {
            FileVo fileVo = FileVo.builder()
                    .postId(postId)
                    .originalName(fileRequestDto.originalName())
                    .savedName(fileRequestDto.savedName())
                    .savedPath(fileRequestDto.savedPath())
                    .savedSize(fileRequestDto.savedSize())
                    .build();
            result.add(fileVo);
        });

        return result;
    }
}
