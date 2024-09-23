package com.hh.multiboarduserbackend.domain.ask.answer;

import com.hh.multiboarduserbackend.common.vo.CommentVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AskAnswerRepository {

    List<CommentVo> findAllByPostId(Long askPostId);

}
