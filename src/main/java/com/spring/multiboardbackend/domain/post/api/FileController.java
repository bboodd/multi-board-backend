package com.spring.multiboardbackend.domain.post.api;

import com.spring.multiboardbackend.domain.post.docs.FileControllerDocs;
import com.spring.multiboardbackend.domain.post.service.FileService;
import com.spring.multiboardbackend.domain.post.dto.response.FileResponse;
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

    // 첨부파일 다운로드
    @GetMapping("/{fileId}/download")
    public ResponseEntity<Resource> downloadFile(@PathVariable String boardType, @PathVariable Long postId, @PathVariable Long fileId) {

        FileResponse fileResponse = fileService.findById(fileId);

        String fileName = URLEncoder.encode(fileResponse.originalName(), StandardCharsets.UTF_8);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, ContentDisposition.attachment()
                        .filename(fileName)
                        .build()
                        .toString())
                .header(HttpHeaders.CONTENT_LENGTH, fileResponse.fileSize() + "")
                .body(fileService.downloadFile(fileId));
    }
}
