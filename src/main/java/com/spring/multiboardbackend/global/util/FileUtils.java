package com.spring.multiboardbackend.global.util;

import com.spring.multiboardbackend.domain.post.exception.FileErrorCode;
import com.spring.multiboardbackend.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnailator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class FileUtils {
    private static final String THUMBNAIL_PREFIX = "thumbnail_";
    private static final int THUMBNAIL_WIDTH = 250;
    private static final int THUMBNAIL_HEIGHT = 150;

    private final S3Client s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    /**
     * 다중 파일 업로드
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
     */
    public UploadedFile uploadFile(MultipartFile multipartFile) {
        if(multipartFile.isEmpty()) {
            return null;
        }

        String savedName = generateSaveFileName(multipartFile.getOriginalFilename());

        try {
            PutObjectRequest objectRequest = PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(savedName)
                    .contentType(multipartFile.getContentType())
                    .build();

            s3Client.putObject(objectRequest,
                    RequestBody.fromInputStream(multipartFile.getInputStream(), multipartFile.getSize()));

            String savedPath = String.format("https://%s.s3.%s.amazonaws.com",
                    bucket, s3Client.serviceClientConfiguration().region());

            return UploadedFile.of(
                    multipartFile.getOriginalFilename(),
                    savedName,
                    savedPath,
                    multipartFile.getSize(),
                    multipartFile.getContentType()
            );

        } catch (IOException e) {
            log.error("파일 업로드 실패: {}", e.getMessage());
            throw ErrorCode.INTERNAL_SERVER_ERROR.defaultException(e);
        }
    }

    /**
     * 썸네일 업로드
     */
    public UploadedFile uploadThumbnail(UploadedFile file) {
        Path tempFile = null;
        Path thumbnailPath = null;

        try {

            // s3에서 객체 가져오기
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucket)
                    .key(file.savedName())
                    .build();

            // s3임시파일 스트림 처리
            ResponseInputStream<GetObjectResponse> s3Object = s3Client.getObject(getObjectRequest);
            tempFile = Files.createTempFile("original_", "_" + System.nanoTime());
            Files.copy(s3Object, tempFile, StandardCopyOption.REPLACE_EXISTING);

            // 썸네일 생성
            String thumbnailName = THUMBNAIL_PREFIX + file.savedName();
            thumbnailPath = Files.createTempFile("thumb_", "_" + System.nanoTime());

            Thumbnailator.createThumbnail(
                    tempFile.toFile(),
                    thumbnailPath.toFile(),
                    THUMBNAIL_WIDTH,
                    THUMBNAIL_HEIGHT
            );

            // 썸네일 S3 업로드
            PutObjectRequest thumbnailRequest = PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(thumbnailName)
                    .contentType(file.contentType())
                    .build();

            s3Client.putObject(thumbnailRequest, RequestBody.fromFile(thumbnailPath));

            // S3에 업로드된 썸네일의 크기 확인
            HeadObjectResponse headObjectResponse = s3Client.headObject(HeadObjectRequest.builder()
                    .bucket(bucket)
                    .key(thumbnailName)
                    .build());

            return UploadedFile.of(
                    file.originalName(),
                    thumbnailName,
                    file.savedPath(),
                    headObjectResponse.contentLength(),
                    file.contentType()
            );

        } catch (IOException e) {
            log.error("썸네일 생성 실패: {}", e.getMessage());
            throw ErrorCode.INTERNAL_SERVER_ERROR.defaultException(e);
        } finally {
            // 임시 파일들 정리
            try {
                if (tempFile != null) {
                    Files.deleteIfExists(tempFile);
                }
                if (thumbnailPath != null) {
                    Files.deleteIfExists(thumbnailPath);
                }
            } catch (IOException e) {
                log.warn("임시 파일 삭제 실패: {}", e.getMessage());
            }
        }
    }

    /**
     * 파일 삭제
     */
    public void deleteFiles(List<UploadedFile> files) {
        if(CollectionUtils.isEmpty(files)) {
            return;
        }

        files.forEach(file -> {
            DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder()
                    .bucket(bucket)
                    .key(file.savedName())
                    .build();

            s3Client.deleteObject(deleteRequest);

            // 이미지일 경우 썸네일이 있다면 삭제
            if (file.contentType().startsWith("image")) {
                String thumbnailName = THUMBNAIL_PREFIX + file.savedName();
                DeleteObjectRequest thumbnailDeleteRequest = DeleteObjectRequest.builder()
                        .bucket(bucket)
                        .key(thumbnailName)
                        .build();
                s3Client.deleteObject(thumbnailDeleteRequest);
            }
        });
    }

    /**
     * 파일 다운로드를 위한 Resource 반환
     */
    public Resource readFileAsResource(UploadedFile file) {
        try {
            String fileUrl = file.savedPath() + "/" + file.savedName();
            return new UrlResource(fileUrl);
        } catch (MalformedURLException e) {
            log.error("파일 리소스 반환 실패: {}", e.getMessage());
            throw FileErrorCode.FILE_NOT_FOUND.defaultException(e);
        }
    }

    private String generateSaveFileName(String originalName) {
        String uuid = UUID.randomUUID().toString().replace("-", "");
        String extension = StringUtils.getFilenameExtension(originalName);
        return uuid + "." + extension;
    }
}