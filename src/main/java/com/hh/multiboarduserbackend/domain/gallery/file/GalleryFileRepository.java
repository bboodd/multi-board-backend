package com.hh.multiboarduserbackend.domain.gallery.file;

import com.hh.multiboarduserbackend.common.vo.FileVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
public interface GalleryFileRepository {

    void saveAll(List<FileVo> fileList);

    FileVo findById(Long galleryFileId);

    List<FileVo> findAllByPostId(Long galleryPostId);

    List<FileVo> findAllByIds(List<Long> idList);

    void deleteAllByIds(List<Long> idList);

    FileVo findFirstByPostId(Long galleryPostId);
}
