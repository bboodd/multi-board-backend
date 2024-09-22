package com.hh.multiboarduserbackend.domain.notice.post;

import com.hh.multiboarduserbackend.common.vo.PostVo;
import com.hh.multiboarduserbackend.common.vo.SearchVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface NoticePostRepository {

    PostVo findById(Long noticePostId);

    List<PostVo> findAllBySearch(SearchVo searchVo);

    int countAllBySearch(SearchVo searchVo);

    void increaseViewCntById(Long noticePostId);
}
