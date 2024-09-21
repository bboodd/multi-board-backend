package com.hh.multiboarduserbackend.domain.gallery.file;


import com.hh.multiboarduserbackend.common.vo.FileVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class GalleryFileService {

    private final GalleryFileRepository galleryFileRepository;

    /**
     * 파일 리스트 저장
     * @param fileList - 파일 리스트
     */
    public void saveFileList(List<FileVo> fileList) {
        galleryFileRepository.saveAll(fileList);
    }

    /**
     * 파일 아이디로 파일 조회
     * @param galleryFileId - pk
     * @return - 파일 정보
     */
    public Optional<FileVo> findById(Long galleryFileId) {
        return Optional.ofNullable(galleryFileRepository.findById(galleryFileId));
    }

    /**
     * 게시글 아이디로 파일 리스트 조회
     * @param galleryPostId - 게시글 아이디
     * @return - 파일 정보 리스트
     */
    public List<FileVo> findAllByPostId(Long galleryPostId) {
        List<FileVo> fileVoList = galleryFileRepository.findAllByPostId(galleryPostId);
        return fileVoList;
    }

    /**
     * 파일 아이디 리스트로 파일 리스트 조회
     * @param idList - pk list
     * @return - 파일 정보 리스트
     */
    public List<FileVo> findAllByIds(List<Long> idList) {
        List<FileVo> fileVoList = galleryFileRepository.findAllByIds(idList);
        return fileVoList;
    }

    /**
     * 파일 아이디 리스트 받아서 삭제
     * @param idList - pk list
     */
    public void deleteAllByIds(List<Long> idList) {
        galleryFileRepository.deleteAllByIds(idList);
    }

    public Optional<FileVo> findFirstByPostId(Long galleryPostId) {
        return Optional.ofNullable(galleryFileRepository.findFirstByPostId(galleryPostId));
    }
}
