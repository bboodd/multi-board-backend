package com.spring.multiboardbackend.domain.post.mappers;

import com.spring.multiboardbackend.domain.post.dto.request.FileRequestDto;
import com.spring.multiboardbackend.domain.post.dto.response.FileResponseDto;
import com.spring.multiboardbackend.domain.post.vo.FileVo;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FileMapper {
    FileResponseDto toDto(FileVo fileVo);

    default List<FileResponseDto> toDtoList(List<FileVo> fileVoList) {
        return fileVoList.stream().map(this::toDto).collect(toList());
    }

    FileVo toVoWithPostId(FileRequestDto fileRequestDto, Long postId);

    default List<FileVo> toVoListWithPostId(List<FileRequestDto> fileList, Long postId) {
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
