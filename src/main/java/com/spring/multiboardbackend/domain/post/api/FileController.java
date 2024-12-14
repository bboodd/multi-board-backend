package com.spring.multiboardbackend.domain.post.api;

import com.spring.multiboardbackend.domain.post.docs.FileControllerDocs;
import com.spring.multiboardbackend.domain.post.dto.response.FileResponse;
import com.spring.multiboardbackend.domain.post.mapper.FileMapper;
import com.spring.multiboardbackend.domain.post.service.FileService;
import com.spring.multiboardbackend.domain.post.vo.FileVO;
import com.spring.multiboardbackend.global.exception.ErrorCode;
import com.spring.multiboardbackend.global.util.FileUtils;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api/posts")
public class FileController implements FileControllerDocs {

    private final FileService fileService;
    private final FileMapper fileMapper;
    private final FileUtils fileUtils;

    /**
     * 첨부파일 다운로드
     */
    @GetMapping("/{postId}/files/{fileId}/download")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long fileId, @PathVariable String boardType, @PathVariable Long postId) {

        try {
            FileVO file = fileService.findById(fileId);
            String objectKey = file.getSavedName();

            String encodedFileName = URLEncoder.encode(file.getOriginalName(), StandardCharsets.UTF_8)
                    .replace("\\+", "%20");

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .contentLength(file.getFileSize())
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''" + encodedFileName)
                    .body(fileUtils.readFileAsResource(objectKey));

        } catch (Exception e) {
            throw ErrorCode.INTERNAL_SERVER_ERROR.defaultException(e);
        }
    }

    @GetMapping("/{postId}/files")
    @Operation(summary = "파일 목록 조회", description = "게시글의 파일 목록을 조회합니다.")
    public ResponseEntity<List<FileResponse>> getFiles(@PathVariable String boardType, @PathVariable Long postId) {
        List<FileVO> files = fileService.findAllByPostId(postId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(fileMapper.toResponseList(files));
    }

    // s3이미지 url 가져오는 메서드
    @GetMapping("/{postId}/files/{fileId}/image")
    @Operation(summary = "파일 이미지 url 조회", description = "이미지 파일에 대한 url을 조회합니다.")
    public ResponseEntity<String> getImageUrl(@PathVariable Long fileId, @PathVariable String boardType, @PathVariable Long postId) {
        FileVO file = fileService.findById(fileId);
        String objectKey = file.getSavedName();
        String preSignedUrl = fileUtils.generatePresignedUrl(objectKey);

        return ResponseEntity.ok(preSignedUrl);
    }

    // 썸네일 url 가져오는 메서드
    @GetMapping("/{postId}/thumbnail")
    @Operation(summary = "썸네일 이미지 url 조회", description = "이미지 썸네일에 대한 url을 조회합니다.")
    public ResponseEntity<String> getThumbnailUrl(@PathVariable Long postId, @PathVariable String boardType) {
        try {
            FileVO file = fileService.findThumbnailByPostId(postId);
            String objectKey = file.getSavedName();
            String preSignedUrl = fileUtils.generatePresignedUrl(objectKey);

            return ResponseEntity.ok(preSignedUrl);
        } catch (Exception e) {
            return ResponseEntity.ok("");
        }
    }
}
