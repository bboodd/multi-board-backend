package com.hh.multiboarduserbackend.common.file;

import com.hh.multiboarduserbackend.common.exception.CustomException;
import com.hh.multiboarduserbackend.domain.file.FileRequestDto;
import com.hh.multiboarduserbackend.domain.file.FileResponseDto;
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
            throw new CustomException("IOException", e);
        }

        return FileRequestDto.builder()
                .fileOriginalName(multipartFile.getOriginalFilename())
                .fileName(saveName)
                .filePath(uploadPath)
                .fileSize(multipartFile.getSize())
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
            deleteFile(file.getFilePath() + file.getFileName());
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
        String fileName = file.getFileName();
        Path filePath = Paths.get(uploadPath, fileName);
        try {
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists() == false || resource.isFile() == false) {
                throw new CustomException("file not found : " + filePath.toString());
            }
            return resource;
        } catch (MalformedURLException e) {
            throw new CustomException("file not found : " + filePath.toString(), e);
        }
    }
}
