package com.spring.multiboardbackend.domain.post.repository;

import com.spring.multiboardbackend.domain.post.vo.FileVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface FileRepository {

    /**
     * 파일 목록 저장
     */
    int saveAll(@Param("fileList") List<FileVO> files);

    /**
     * 파일 단건 조회
     */
    Optional<FileVO> findById(Long id);

    /**
     * 게시글의 첫번째 파일 조회
     */
    Optional<FileVO> findFirstByPostId(Long postId);

    /**
     * 게시글의 모든 파일 조회
     */
    List<FileVO> findAllByPostId(Long postId);

    /**
     * 파일 ID 목록으로 파일 조회
     */
    List<FileVO> findAllByIds(@Param("idList") List<Long> ids);

    /**
     * 썸네일 저장
     */
    int saveThumbnail(@Param("file") FileVO file);


    /**
     * 썸네일 존재 여부 확인
     */
    boolean existsThumbnailByPostId(Long postId);

    /**
     * 파일 ID 목록으로 파일 삭제
     */
    int deleteAllByIds(@Param("idList") List<Long> ids);


}
