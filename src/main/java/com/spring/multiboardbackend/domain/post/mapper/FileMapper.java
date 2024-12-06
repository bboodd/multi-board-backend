package com.spring.multiboardbackend.domain.post.mapper;

import com.spring.multiboardbackend.domain.post.dto.response.FileResponse;
import com.spring.multiboardbackend.domain.post.vo.FileVO;
import com.spring.multiboardbackend.global.util.UploadedFile;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FileMapper {

    // FileVo -> FileResponse 변환
    FileResponse toResponse(FileVO fileVo);

    List<FileResponse> toResponseList(List<FileVO> fileVOList);

    FileVO toVO(UploadedFile uploadedFile, Long postId);

    FileVO toThumbnailVO(UploadedFile uploadedFile, Long postId, Long id);

    // List<FileRequest> -> List<FileVo> 변환
    default List<FileVO> toVOList(List<UploadedFile> uploadedFiles, Long postId) {
        return uploadedFiles.stream()
                .map(uploadedFile -> toVO(uploadedFile, postId))
                .toList();
    }

    UploadedFile toUploadedFile(FileVO file);

    List<UploadedFile> toUploadedFileList(List<FileVO> fileVOList);

}
