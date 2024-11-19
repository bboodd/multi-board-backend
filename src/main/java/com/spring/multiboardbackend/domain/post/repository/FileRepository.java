package com.spring.multiboardbackend.domain.post.repository;

import com.spring.multiboardbackend.domain.post.vo.FileVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FileRepository {

    void saveAll(List<FileVo> fileList);

    FileVo findById(Long fileId);

    List<FileVo> findAllByPostId(Long postId);

    List<FileVo> findAllByIds(List<Long> idList);

    void deleteAllByIds(List<Long> idList);

    FileVo findFirstByPostId(Long postId);

    void saveThumbnail(FileVo fileVo);

    int countAllThumbnailByPostId(Long postId);
}
