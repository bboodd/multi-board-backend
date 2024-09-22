package com.hh.multiboarduserbackend.domain.gallery.file;

import com.hh.multiboarduserbackend.common.dto.response.FileResponseDto;
import com.hh.multiboarduserbackend.common.response.Response;
import com.hh.multiboarduserbackend.common.utils.FileUtils;
import com.hh.multiboarduserbackend.common.vo.FileVo;
import com.hh.multiboarduserbackend.domain.free.file.FreeFileService;
import com.hh.multiboarduserbackend.exception.FileErrorCode;
import com.hh.multiboarduserbackend.mappers.FileMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;


@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api-board/gallery-board")
public class GalleryFileController {

    private final GalleryFileService galleryFileService;
    private final FileUtils fileUtils;

    private final FileMapper fileModelMapper = Mappers.getMapper(FileMapper.class);

    // 파일 리스트 조회
    @GetMapping("/posts/{galleryPostId}/files")
    public ResponseEntity<?> getFiles(@PathVariable Long galleryPostId) {

        List<FileVo> fileVoList = galleryFileService.findAllByPostId(galleryPostId);
        List<FileResponseDto> fileDtoList = fileModelMapper.toDtoList(fileVoList);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(Response.data(fileDtoList));
    }

    // 첨부파일 다운로드
    @GetMapping("/posts/{galleryPostId}/files/{galleryFileId}/download")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long galleryPostId, @PathVariable Long galleryFileId) {

        FileVo fileVo = galleryFileService.findById(galleryFileId).orElseThrow(FileErrorCode.FILE_NOT_FOUND::defaultException);
        FileResponseDto fileResponseDto = fileModelMapper.toDto(fileVo);

        Resource resource = fileUtils.readFileAsResource(fileResponseDto);
        String fileName = URLEncoder.encode(fileResponseDto.originalName(), StandardCharsets.UTF_8);
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, ContentDisposition.attachment()
                        .filename(fileName)
                        .build()
                        .toString())
                .header(HttpHeaders.CONTENT_LENGTH, fileResponseDto.savedSize() + "")
                .body(resource);
    }
}
