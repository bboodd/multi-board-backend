package com.hh.multiboarduserbackend.domain.gallery.thumbnail;


import com.hh.multiboarduserbackend.common.vo.FileVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.util.List;

@Mapper
public interface GalleryThumbnailRepository {

    void save(FileVo fileVo);

    int countAllByPostId(Long galleryPostId);

}
