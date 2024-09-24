package com.hh.multiboarduserbackend.domain.file;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileService {

    private final FileRepository fileRepository;

    /**
     * 파일 리스트 저장
     * @param fileList - 파일 리스트
     */
    public void saveFileList(List<FileVo> fileList) {
        fileRepository.saveAll(fileList);
    }

    /**
     * 파일 아이디로 파일 조회
     * @param fileId - pk
     * @return - 파일 정보
     */
    public Optional<FileVo> findById(Long fileId) {
        return Optional.ofNullable(fileRepository.findById(fileId));
    }

    /**
     * 게시글 아이디로 파일 리스트 조회
     * @param postId - 게시글 아이디
     * @return - 파일 정보 리스트
     */
    public List<FileVo> findAllByPostId(Long postId) {
        List<FileVo> fileVoList = fileRepository.findAllByPostId(postId);
        return fileVoList;
    }

    /**
     * 파일 아이디 리스트로 파일 리스트 조회
     * @param idList - pk list
     * @return - 파일 정보 리스트
     */
    public List<FileVo> findAllByIds(List<Long> idList) {
        List<FileVo> fileVoList = fileRepository.findAllByIds(idList);
        return fileVoList;
    }

    /**
     * 파일 아이디 리스트 받아서 삭제
     * @param idList - pk list
     */
    public void deleteAllByIds(List<Long> idList) {
        fileRepository.deleteAllByIds(idList);
    }

    public Optional<FileVo> findFirstByPostId(Long galleryPostId) {
        return Optional.ofNullable(fileRepository.findFirstByPostId(galleryPostId));
    }

    /**
     * 썸네일 저장
     * @param fileVo - 썸네일 파일 정보
     */
    public void saveThumbnail(FileVo fileVo) {
        fileRepository.saveThumbnail(fileVo);
    }

    /**
     * 게시글의 썸네일 존재하는지 확인하는 메서드
     * @param postId - fk
     * @return - true or false
     */
    public boolean checkExistsThumbnail(Long postId) {
        int count = fileRepository.countAllThumbnailByPostId(postId);
        return count != 0;
    }
}
