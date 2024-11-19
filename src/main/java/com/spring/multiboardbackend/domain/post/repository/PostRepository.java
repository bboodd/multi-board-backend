package com.spring.multiboardbackend.domain.post.repository;

import com.spring.multiboardbackend.domain.post.vo.PostVo;
import com.spring.multiboardbackend.global.common.vo.SearchVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PostRepository {

    void save(PostVo postVo);

    PostVo findById(Long postId);

    void update(PostVo postVo);

    void deleteById(Long postId);

    List<PostVo> findAllBySearch(SearchVo searchVo);

    int countAllBySearch(SearchVo searchVo);

    void increaseViewCntById(Long postId);
}
