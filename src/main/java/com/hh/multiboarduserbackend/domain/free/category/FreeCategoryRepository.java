package com.hh.multiboarduserbackend.domain.free.category;

import com.hh.multiboarduserbackend.common.vo.CategoryVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
public interface FreeCategoryRepository {

    List<CategoryVo> findAll();
}
