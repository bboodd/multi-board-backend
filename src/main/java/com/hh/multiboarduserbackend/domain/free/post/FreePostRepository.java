package com.hh.multiboarduserbackend.domain.free.post;

import com.hh.multiboarduserbackend.common.vo.SearchVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
public interface FreePostRepository {

    void save(FreePostVo freePostVo);

    FreePostVo findById(Long freePostId);

    void update(FreePostVo freePostVo);

    void deleteById(Long freePostId);

    List<FreePostVo> findAllBySearch(SearchVo searchVo);

    int countAllBySearch(SearchVo searchVo);

    void increaseViewCntById(Long freePostId);
}
