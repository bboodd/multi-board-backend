package com.hh.multiboarduserbackend.domain.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    /**
     * 카테고리 리스트 조회
     * @return - 리스트
     */
    public List<CategoryVo> findAll(Long typeId) {
        return categoryRepository.findAll(typeId);
    }
}
