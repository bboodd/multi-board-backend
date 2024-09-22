package com.hh.multiboarduserbackend.domain.ask.post;

import com.hh.multiboarduserbackend.common.vo.PostVo;
import com.hh.multiboarduserbackend.common.vo.SearchVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
public interface AskPostRepository {

    void save(PostVo postVo);

    PostVo findById(Long askPostId);

    void update(PostVo postVo);

    void deleteById(Long askPostId);

    List<PostVo> findAllBySearch(SearchVo searchVo);

    int countAllBySearch(SearchVo searchVo);

    void increaseViewCntById(Long askPostId);
}
