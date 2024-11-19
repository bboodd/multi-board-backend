package com.spring.multiboardbackend.domain.board.repository;

import com.spring.multiboardbackend.domain.board.vo.CategoryVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CategoryRepository {

    List<CategoryVo> findAll(Long typeId);
}
