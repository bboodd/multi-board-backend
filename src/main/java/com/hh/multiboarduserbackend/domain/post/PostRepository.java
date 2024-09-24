package com.hh.multiboarduserbackend.domain.post;

import com.hh.multiboarduserbackend.common.vo.SearchVo;
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
