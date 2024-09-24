package com.hh.multiboarduserbackend.domain.category;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CategoryRepository {

    List<CategoryVo> findAll(Long typeId);
}
