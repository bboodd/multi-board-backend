package com.hh.multiboarduserbackend.domain.notice.category;

import com.hh.multiboarduserbackend.common.vo.CategoryVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface NoticeCategoryRepository {

    List<CategoryVo> findAll();

}
