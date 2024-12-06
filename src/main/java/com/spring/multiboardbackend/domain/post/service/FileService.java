package com.spring.multiboardbackend.domain.post.service;

import com.spring.multiboardbackend.domain.post.exception.FileErrorCode;
import com.spring.multiboardbackend.domain.post.repository.FileRepository;
import com.spring.multiboardbackend.domain.post.vo.FileVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FileService {

    private final FileRepository fileRepository;

    /**
     * 여러 파일을 저장합니다.
     *
     * @param files 저장할 파일 목록
     * @return 저장된 파일 목록
     */
    @Transactional(readOnly = false)
    public List<FileVO> saveFiles(List<FileVO> files) {
        fileRepository.saveAll(files);

        return files;
    }

    /**
     * 지정된 ID 목록의 파일들을 삭제합니다.
     *
     * @param removeIds 삭제할 파일 ID 목록
     */
    @Transactional(readOnly = false)
    public void deleteAllByIds(List<Long> removeIds) {
        fileRepository.deleteAllByIds(removeIds);
    }

    public List<FileVO> findAllByPostId(Long postId) {
        return fileRepository.findAllByPostId(postId);
    }

    /**
     * 파일 ID로 파일 정보를 조회합니다.
     *
     * @param id 조회할 파일의 ID
     * @return 파일 정보
     */
    public FileVO findById(Long id) {
        return fileRepository.findById(id)
                .orElseThrow(FileErrorCode.FILE_NOT_FOUND::defaultException);
    }

    /**
     * 게시글에 첨부된 첫 번째 파일을 조회합니다.
     *
     * @param postId 게시글 ID
     * @return 첫 번째 파일 정보
     */
    public FileVO findFirstByPostId(Long postId) {
        return fileRepository.findFirstByPostId(postId)
                .orElseThrow(FileErrorCode.FILE_NOT_FOUND::defaultException);
    }

    /**
     * 지정된 ID 목록의 파일들을 조회합니다.
     *
     * @param ids 조회할 파일 ID 목록
     * @return 파일 정보 목록
     */
    public List<FileVO> findAllByIds(List<Long> ids) {
        return fileRepository.findAllByIds(ids);
    }

    /**
     * 게시글의 썸네일 이미지를 저장합니다.
     *
     * @param thumbnail 저장할 썸네일 정보
     */
    @Transactional(readOnly = false)
    public void saveThumbnail(FileVO thumbnail) {
        fileRepository.saveThumbnail(thumbnail);
    }

    public FileVO findThumbnailByPostId(Long postId) {
        return fileRepository.findThumbnailByPostId(postId)
                .orElseThrow(FileErrorCode.FILE_NOT_FOUND::defaultException);
    }
}