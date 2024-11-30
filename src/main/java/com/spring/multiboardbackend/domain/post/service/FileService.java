package com.spring.multiboardbackend.domain.post.service;

import com.spring.multiboardbackend.domain.board.enums.BoardType;
import com.spring.multiboardbackend.domain.post.dto.response.FileResponse;
import com.spring.multiboardbackend.domain.post.exception.FileErrorCode;
import com.spring.multiboardbackend.domain.post.mapper.FileMapper;
import com.spring.multiboardbackend.domain.post.repository.FileRepository;
import com.spring.multiboardbackend.domain.post.vo.FileVO;
import com.spring.multiboardbackend.global.util.FileUtils;
import com.spring.multiboardbackend.global.util.UploadedFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FileService {

    private final FileRepository fileRepository;
    private final FileMapper fileMapper;
    private final FileUtils fileUtils;

    /**
     * 파일 목록 저장 처리
     *
     * @param files 저장할 파일 목록
     * @param postId 게시글 ID
     * @param type 게시판 타입
     * @return 저장된 파일 목록 Response
     */
    @Transactional
    public List<FileResponse> saveFiles(List<MultipartFile> files, Long postId, BoardType type) {
        return processFiles(files, postId, type);
    }

    /**
     * 파일 목록 수정 처리
     *
     * @param removeIds 삭제할 파일 ID 목록
     * @param newFiles 새로 추가할 파일 목록
     * @param postId 게시글 ID
     * @param type 게시판 타입
     * @return 수정된 파일 목록 Response
     */
    @Transactional
    public List<FileResponse> updateFiles(List<Long> removeIds, List<MultipartFile> newFiles, Long postId, BoardType type) {
        // 파일 삭제
        if (!CollectionUtils.isEmpty(removeIds)) {
            List<FileVO> files = fileRepository.findAllByIds(removeIds);
            fileUtils.deleteFiles(fileMapper.toUploadedFileList(files));
            fileRepository.deleteAllByIds(removeIds);
        }

        // 새로운 파일 업로드
        return processFiles(newFiles, postId, type);
    }

    /**
     * 파일 처리 공통 로직
     *
     * @param files 처리할 파일 목록
     * @param postId 게시글 ID
     * @param type 게시판 타입
     * @return 처리된 파일 목록 Response
     */
    private List<FileResponse> processFiles(List<MultipartFile> files, Long postId, BoardType type) {
        if (CollectionUtils.isEmpty(files)) {
            return Collections.emptyList();
        }

        List<UploadedFile> uploadedFiles = fileUtils.uploadFiles(files);

        List<FileVO> fileVOList = fileMapper.toVOList(uploadedFiles, postId);

        fileRepository.saveAll(fileVOList);

        if (type.equals(BoardType.GALLERY)) {
            processThumbnail(postId);
        }

        return fileMapper.toResponseList(fileVOList);
    }

    /**
     * ID로 파일 조회
     *
     * @param id 조회할 파일 ID
     * @return 파일 정보 Response
     */
    public FileResponse findById(Long id) {
        FileVO fileVo = fileRepository.findById(id)
                .orElseThrow(FileErrorCode.FILE_NOT_FOUND::defaultException);
        return fileMapper.toResponse(fileVo);
    }

    /**
     * 게시글의 파일 목록 조회
     *
     * @param postId 게시글 ID
     * @return 파일 목록 Response
     */
    public List<FileResponse> findAllByPostId(Long postId) {
        List<FileVO> files = fileRepository.findAllByPostId(postId);
        return fileMapper.toResponseList(files);
    }

    /**
     * 게시글의 썸네일 존재 여부 확인
     *
     * @param postId 게시글 ID
     * @return 썸네일 존재 여부
     */
    private boolean hasThumbnail(Long postId) {
        return fileRepository.existsThumbnailByPostId(postId);
    }

    /**
     * 게시글 썸네일 처리
     *
     * @param postId 게시글 ID
     */
    private void processThumbnail(Long postId) {
        if (!hasThumbnail(postId)) {
            FileVO fileVo = fileRepository.findFirstByPostId(postId)
                    .orElseThrow(FileErrorCode.FILE_NOT_FOUND::defaultException);

            UploadedFile thumbnail = fileUtils.uploadThumbnail(fileMapper.toUploadedFile(fileVo));

            fileRepository.saveThumbnail(fileMapper.toThumbnailVO(thumbnail, postId, fileVo.getId()));
        }
    }

    /**
     * 파일 다운로드
     *
     * @param fileId 다운로드할 파일 ID
     * @return 파일 Resource
     */
    public Resource downloadFile(Long fileId) {
        FileVO file = fileRepository.findById(fileId)
                .orElseThrow(FileErrorCode.FILE_NOT_FOUND::defaultException);

        return fileUtils.readFileAsResource(fileMapper.toUploadedFile(file));
    }
}