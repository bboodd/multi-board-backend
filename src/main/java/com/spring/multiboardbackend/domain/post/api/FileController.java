package com.spring.multiboardbackend.domain.post.api;

import com.spring.multiboardbackend.domain.post.service.FileService;
import com.spring.multiboardbackend.domain.post.vo.FileVo;
import com.spring.multiboardbackend.global.common.response.Response;
import com.spring.multiboardbackend.global.util.FileUtils;
import com.spring.multiboardbackend.domain.post.dto.response.FileResponseDto;
import com.spring.multiboardbackend.domain.post.exception.FileErrorCode;
import com.spring.multiboardbackend.domain.post.mappers.FileMapper;
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
@RequestMapping("/api-board")
public class FileController {

    private final FileService fileService;
    private final FileUtils fileUtils;

    private final FileMapper fileModelMapper = Mappers.getMapper(FileMapper.class);

    // 파일 리스트 조회
    @GetMapping("/{boardType}/posts/{postId}/files")
    public ResponseEntity<?> getFiles(@PathVariable String boardType, @PathVariable Long postId) {

        List<FileVo> fileVoList = fileService.findAllByPostId(postId);
        List<FileResponseDto> fileDtoList = fileModelMapper.toDtoList(fileVoList);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(Response.data(fileDtoList));
    }

    // 첨부파일 다운로드
    @GetMapping("/{boardType}/posts/{postId}/files/{fileId}/download")
    public ResponseEntity<Resource> downloadFile(@PathVariable String boardType, @PathVariable Long postId, @PathVariable Long fileId) {

        FileVo fileVo = fileService.findById(fileId).orElseThrow(FileErrorCode.FILE_NOT_FOUND::defaultException);
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
