package com.hh.multiboarduserbackend.domain.gallery.category;

import com.hh.multiboarduserbackend.common.vo.CategoryVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
public interface GalleryCategoryRepository {

    List<CategoryVo> findAll();
}
