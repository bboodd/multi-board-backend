package com.spring.multiboardbackend.domain.post.api;

import com.spring.multiboardbackend.domain.post.docs.FileControllerDocs;
import com.spring.multiboardbackend.domain.post.mapper.FileMapper;
import com.spring.multiboardbackend.domain.post.service.FileService;
import com.spring.multiboardbackend.domain.post.vo.FileVO;
import com.spring.multiboardbackend.global.util.FileUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api/files")
public class FileController implements FileControllerDocs {

    private final FileService fileService;
    private final FileMapper fileMapper;
    private final FileUtils fileUtils;

    /**
     * 첨부파일 다운로드
     */
    @GetMapping("/{fileId}/download")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long fileId) {

        FileVO file = fileService.findById(fileId);

        String fileName = URLEncoder.encode(file.getOriginalName(), StandardCharsets.UTF_8);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, ContentDisposition.attachment()
                        .filename(fileName)
                        .build()
                        .toString())
                .header(HttpHeaders.CONTENT_LENGTH, file.getFileSize() + "")
                .body(fileUtils.readFileAsResource(fileMapper.toUploadedFile(file)));
    }
}
