package com.spring.multiboardbackend.global.util;

import com.spring.multiboardbackend.domain.post.enums.FileType;
import com.spring.multiboardbackend.domain.post.exception.FileErrorCode;
import com.spring.multiboardbackend.global.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnailator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@Slf4j
public class FileUtils {
    private static final String THUMBNAIL_PREFIX = "thumbnail_";
    private static final int THUMBNAIL_WIDTH = 250;
    private static final int THUMBNAIL_HEIGHT = 150;

    @Value("${upload-path}")
    private String uploadPath;

    /**
     * 다중 파일 업로드
     * @param multipartFileList - 파일 객체 list
     * @return db에 저장할 파일 정보 list
     */
    public List<UploadedFile> uploadFiles(List<MultipartFile> multipartFileList) {
        List<UploadedFile> files = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFileList) {
            if(multipartFile.isEmpty()) {
                continue;
            }
            files.add(uploadFile(multipartFile));
        }
        return files;
    }

    /**
     * 단일 파일 업로드
     * @param multipartFile - 파일 객체
     * @return db에 저장할 파일 정보
     */
    public UploadedFile uploadFile(MultipartFile multipartFile) {
        if(multipartFile.isEmpty()) {
            return null;
        }

        String saveName = generateSaveFileName(multipartFile.getOriginalFilename());
        Path filePath = Paths.get(uploadPath, saveName);

        try {
            Files.createDirectories(filePath.getParent()); // 디렉토리가 없을 경우 생성
            Files.copy(multipartFile.getInputStream(), filePath);

            return UploadedFile.of(
                    multipartFile.getOriginalFilename(),
                    saveName,
                    uploadPath,
                    multipartFile.getSize(),
                    FileType.ATTACHMENT,
                    multipartFile.getContentType()
            );

        } catch (IOException e) {
            log.error("파일 업로드 실패: {}", e.getMessage());
            throw ErrorCode.INTERNAL_SERVER_ERROR.defaultException(e);
        }
    }

    /**
     * File 매개변수가 이미지타입인지 확인
     * @param path - 파일 경로
     * @return - boolean
     */
    private boolean checkImageType(Path path) {
        try {
            String contentType = Files.probeContentType(path);
            return contentType != null && contentType.startsWith("image");
        } catch (IOException e) {
            throw ErrorCode.INTERNAL_SERVER_ERROR.defaultException(e);
        }
    }

    /**
     * 썸네일 업로드 메서드
     * @param file - 썸네일로 변환할 파일 정보
     * @return - FileVo
     */
    public UploadedFile uploadThumbnail(UploadedFile file) {
        Path originPath = Paths.get(uploadPath, file.savedName());
        String thumbnailName = THUMBNAIL_PREFIX + file.savedName();
        Path thumbnailPath = Paths.get(uploadPath, thumbnailName);

        try {
            if (!Files.exists(originPath)) {
                throw FileErrorCode.FILE_NOT_FOUND.defaultException();
            }

            if (!checkImageType(originPath)) {
                throw FileErrorCode.FILE_NOT_IMAGE.defaultException();
            }

            Thumbnailator.createThumbnail(
                    originPath.toFile(),
                    thumbnailPath.toFile(),
                    THUMBNAIL_WIDTH,
                    THUMBNAIL_HEIGHT
            );

            return UploadedFile.of(
                    file.originalName(),
                    thumbnailName,
                    uploadPath,
                    Files.size(thumbnailPath),
                    FileType.THUMBNAIL,
                    Files.probeContentType(thumbnailPath)
            );

        } catch (IOException e) {
            log.error("썸네일 생성 실패: {}", e.getMessage());
            throw ErrorCode.INTERNAL_SERVER_ERROR.defaultException(e);

        }
    }

    /**
     * 저장 파일명 생성
     * @param originalName 원본 파일명
     * @return 디스크에 저장할 파일명
     */
    private String generateSaveFileName(String originalName) {
        String uuid = UUID.randomUUID().toString().replace("-", "");
        String extension = StringUtils.getFilenameExtension(originalName);
        return uuid + "." + extension;
    }

    /**
     * 파일 삭제 (from disk)
     * @param files - 삭제할 파일 정보 list
     */
    public void deleteFiles(List<UploadedFile> files) {
        if(CollectionUtils.isEmpty(files)) {
            return;
        }
        files.forEach(file -> {
            try {
                Path filePath = Paths.get(file.savedPath(), file.savedName());
                Files.deleteIfExists(filePath);
            } catch (IOException e) {
                log.error("파일 삭제 실패: {}", e.getMessage());
                throw ErrorCode.INTERNAL_SERVER_ERROR.defaultException(e);
            }
        });
    }

    /**
     * @param file - 파일 정보
     * @return - Resource
     */
    public Resource readFileAsResource(UploadedFile file) {
        try {
            Path filePath = Paths.get(uploadPath, file.savedName());
            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists() || !resource.isFile()) {
                throw FileErrorCode.FILE_NOT_FOUND.defaultException();
            }

            return resource;
        } catch (MalformedURLException e) {
            log.error("파일 리소스 반환 실패: {}", e.getMessage());
            throw FileErrorCode.FILE_NOT_FOUND.defaultException(e);
        }
    }
}
