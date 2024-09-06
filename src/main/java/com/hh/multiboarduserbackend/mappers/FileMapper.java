package com.hh.multiboarduserbackend.mappers;

import com.hh.multiboarduserbackend.common.dto.request.FileRequestDto;
import com.hh.multiboarduserbackend.common.dto.response.FileResponseDto;
import com.hh.multiboarduserbackend.common.vo.FileVo;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FileMapper {

    FileMapper INSTANCE = Mappers.getMapper(FileMapper.class);

    FileResponseDto toDto(FileVo fileVo);

    @Named("toVo")
    FileVo toVo(FileRequestDto fileRequestDto, Long postId);

    @IterableMapping(qualifiedByName = "toVo")
    List<FileVo> toVoList(List<FileRequestDto> fileList, Long postId);
}
