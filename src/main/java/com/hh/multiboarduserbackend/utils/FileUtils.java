package com.hh.multiboarduserbackend.common.utils;

import com.hh.multiboarduserbackend.domain.file.request.FileRequestDto;
import com.hh.multiboarduserbackend.domain.file.response.FileResponseDto;
import com.hh.multiboarduserbackend.domain.file.FileVo;
import com.hh.multiboarduserbackend.exception.FileErrorCode;
import net.coobird.thumbnailator.Thumbnailator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class FileUtils {

    @Value("${upload-path}")
    private String uploadPath;

    /**
     * 다중 파일 업로드
     * @param multipartFileList - 파일 객체 list
     * @return db에 저장할 파일 정보 list
     */
    public List<FileRequestDto> uploadFiles(List<MultipartFile> multipartFileList) {
        List<FileRequestDto> files = new ArrayList<>();
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
    public FileRequestDto uploadFile(MultipartFile multipartFile) {
        if(multipartFile.isEmpty()) {
            return null;
        }

        String saveName = generateSaveFileName(multipartFile.getOriginalFilename());
        String filePath = getUploadPath() + File.separator + saveName;
        File uploadFile = new File(filePath);

        try {
            multipartFile.transferTo(uploadFile);
        } catch (IOException e) {
            throw FileErrorCode.DEFAULT_ERROR_CODE.defaultException(e);
        }

        return FileRequestDto.builder()
                .originalName(multipartFile.getOriginalFilename())
                .savedName(saveName)
                .savedPath(uploadPath)
                .savedSize(multipartFile.getSize())
                .build();
    }

    /**
     * File 매개변수가 이미지타입인지 확인
     * @param file - File
     * @return - boolean
     */
    private boolean checkImageType(File file) {
        try {
            String contentType = Files.probeContentType(file.toPath());
            return contentType.startsWith("image");
        } catch (IOException e) {
            throw FileErrorCode.DEFAULT_ERROR_CODE.defaultException(e);
        }
    }

    /**
     * 썸네일 업로드 메서드
     * @param fileVo - 썸네일로 변환할 파일 정보
     * @return - FileVo
     */
    public FileVo uploadThumbnail(FileVo fileVo) {
        String originFilePath = getUploadPath() + File.separator + fileVo.getSavedName();

        String thumbnailSaveName = "thumbnail_" + fileVo.getSavedName();
        String thumbnailFilePath = getUploadPath() + File.separator + thumbnailSaveName;

        long thumbnailSize = 0;

        try {
            File originFile = new File(originFilePath);
            if(!checkImageType(originFile)) {
                throw FileErrorCode.FILE_NOT_IMAGE.defaultException();
            }
            File thumbnailFile = new File(thumbnailFilePath);

            Thumbnailator.createThumbnail(originFile, thumbnailFile, 250, 150);

            thumbnailSize = thumbnailFile.length();
        } catch (IOException e) {
            throw FileErrorCode.DEFAULT_ERROR_CODE.defaultException(e);
        }

        return FileVo.builder()
                .fileId(fileVo.getFileId())
                .postId(fileVo.getPostId())
                .originalName(fileVo.getOriginalName())
                .savedName(thumbnailSaveName)
                .savedPath(uploadPath)
                .savedSize(thumbnailSize)
                .build();
    }

    /**
     * 저장 파일명 생성
     * @param originalName 원본 파일명
     * @return 디스크에 저장할 파일명
     */
    private String generateSaveFileName(String originalName) {
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        String extension = StringUtils.getFilenameExtension(originalName);
        return uuid + "." + extension;
    }

    /**
     * 업로드 경로 반환
     * @return 업로드 경로
     */
    private String getUploadPath() {
        return makeDirectories(uploadPath);
    }

    /**
     * 업로드 경로 반환
     * @param addPath - 추가 경로
     * @return 업로드 경로
     */
    private String getUploadPath(String addPath) {
        return makeDirectories(uploadPath + File.separator + addPath);
    }

    /**
     * 업로드 폴더(디렉터리) 생성
     * @param filePath - 업로드 경로
     * @return 업로드 경로
     */
    private String makeDirectories(String filePath) {
        File dir = new File(filePath);
        if(dir.exists() == false) {
            dir.mkdir();
        }
        return dir.getPath();
    }

    /**
     * 파일 삭제 (from disk)
     * @param files - 삭제할 파일 정보 list
     */
    public void deleteFiles(List<FileResponseDto> files) {
        if(CollectionUtils.isEmpty(files)) {
            return;
        }
        for(FileResponseDto file : files) {
            deleteFile(file.savedPath() + file.savedName());
        }
    }

    /**
     * 파일 삭제 (from DisK)
     * @param addPath - 추가 경로
     * @param fileName - 파일명
     */
    private void deleteFile(String addPath, String fileName) {
        String filePath = Paths.get(uploadPath, addPath, fileName).toString();
        deleteFile(filePath);
    }

    /**
     * 파일 삭제 (from Disk)
     * @param filePath - 파일 경로
     */
    private void deleteFile(String filePath) {
        File file = new File(filePath);
        if(file.exists()) {
            file.delete();
        }
    }

    /**
     * 파일 dto 받아서 resource 반환
     * @param file - 파일 정보
     * @return - Resource
     */
    public Resource readFileAsResource(FileResponseDto file) {
        String fileName = file.savedName();
        Path filePath = Paths.get(uploadPath, fileName);
        try {
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists() == false || resource.isFile() == false) {
                throw FileErrorCode.FILE_NOT_FOUND.defaultException();
            }
            return resource;
        } catch (MalformedURLException e) {
            throw FileErrorCode.FILE_NOT_FOUND.defaultException(e);
        }
    }
}
