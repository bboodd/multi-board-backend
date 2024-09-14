package com.hh.multiboarduserbackend.domain.free.post;

import com.hh.multiboarduserbackend.common.vo.PostVo;
import com.hh.multiboarduserbackend.common.vo.SearchVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
public interface FreePostRepository {

    void save(PostVo postVo);

    PostVo findById(Long freePostId);

    void update(PostVo postVo);

    void deleteById(Long freePostId);

    List<PostVo> findAllBySearch(SearchVo searchVo);

    int countAllBySearch(SearchVo searchVo);

    void increaseViewCntById(Long freePostId);
}
