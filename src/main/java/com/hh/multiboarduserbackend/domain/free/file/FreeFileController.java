package com.hh.multiboarduserbackend.domain.free.file;

import com.hh.multiboarduserbackend.common.dto.response.FileResponseDto;
import com.hh.multiboarduserbackend.common.response.Response;
import com.hh.multiboarduserbackend.common.utils.FileUtils;
import com.hh.multiboarduserbackend.common.vo.FileVo;
import com.hh.multiboarduserbackend.exception.FileErrorCode;
import com.hh.multiboarduserbackend.mappers.FileMapper;
import lombok.Generated;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api-board/free-board")
public class FreeFileController {

    private final FreeFileService freeFileService;
    private final FileUtils fileUtils;

    private final FileMapper fileModelMapper = Mappers.getMapper(FileMapper.class);

    // 파일 리스트 조회
    @GetMapping("/posts/{freePostId}/files")
    public ResponseEntity<?> getFiles(@PathVariable Long freePostId) {

        List<FileVo> fileVoList = freeFileService.findAllByPostId(freePostId);
        List<FileResponseDto> fileDtoList = fileModelMapper.toDtoList(fileVoList);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(Response.data(fileDtoList));
    }

    // 첨부파일 다운로드
    @GetMapping("/posts/{freePostId}/files/{freeFileId}/download")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long freePostId, @PathVariable Long freeFileId) {

        FileVo fileVo = freeFileService.findById(freeFileId).orElseThrow(FileErrorCode.FILE_NOT_FOUND::defaultException);
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
