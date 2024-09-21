package com.hh.multiboarduserbackend.domain.gallery.post;

import com.hh.multiboarduserbackend.common.vo.PostVo;
import com.hh.multiboarduserbackend.common.vo.SearchVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
public interface GalleryPostRepository {

    void save(PostVo postVo);

    PostVo findById(Long galleryPostId);

    void update(PostVo postVo);

    void deleteById(Long galleryPostId);

    List<PostVo> findAllBySearch(SearchVo searchVo);

    int countAllBySearch(SearchVo searchVo);

    void increaseViewCntById(Long galleryPostId);

}
