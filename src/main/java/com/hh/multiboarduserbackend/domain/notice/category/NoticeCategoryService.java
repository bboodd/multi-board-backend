package com.hh.multiboarduserbackend.domain.notice.category;

import com.hh.multiboarduserbackend.common.vo.CategoryVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class NoticeCategoryService {

    private final NoticeCategoryRepository noticeCategoryRepository;


    /**
     * 카테고리 리스트 조회
     * @return - 리스트
     */
    public List<CategoryVo> findAll() {
        return noticeCategoryRepository.findAll();
    }
}
