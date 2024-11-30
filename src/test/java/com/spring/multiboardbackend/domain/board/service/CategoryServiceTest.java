package com.spring.multiboardbackend.domain.board.service;

import com.spring.multiboardbackend.domain.board.dto.response.CategoryResponse;
import com.spring.multiboardbackend.domain.board.exception.BoardErrorCode;
import com.spring.multiboardbackend.domain.board.mapper.CategoryMapper;
import com.spring.multiboardbackend.domain.board.repository.CategoryRepository;
import com.spring.multiboardbackend.domain.board.vo.CategoryVO;
import com.spring.multiboardbackend.global.exception.CustomException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("카테고리 서비스 테스트")
class CategoryServiceTest {

    @InjectMocks
    private CategoryService categoryService;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @Nested
    @DisplayName("게시판 타입별 카테고리 목록 조회")
    class FindAllByBoardType {

        @Test
        @DisplayName("조회 성공")
        void findAllByBoardType_Success() {
            // given
            Long boardTypeId = 1L;
            List<CategoryVO> categories = List.of(
                    createCategoryVO(1L, "카테고리1"),
                    createCategoryVO(2L, "카테고리2")
            );
            List<CategoryResponse> expectedResponses = List.of(
                    createCategoryResponse(1L, "카테고리1"),
                    createCategoryResponse(2L, "카테고리2")
            );

            given(categoryRepository.findAllByBoardTypeId(boardTypeId)).willReturn(categories);
            given(categoryMapper.toResponseList(categories)).willReturn(expectedResponses);

            // when
            List<CategoryResponse> result = categoryService.findAllByBoardType(boardTypeId);

            // then
            assertThat(result).hasSize(2);
            assertThat(result.get(0).id()).isEqualTo(1L);
            assertThat(result.get(0).name()).isEqualTo("카테고리1");
            verify(categoryRepository).findAllByBoardTypeId(boardTypeId);
            verify(categoryMapper).toResponseList(categories);
        }

        @Test
        @DisplayName("빈 목록 조회 성공")
        void findAllByBoardType_EmptyList_Success() {
            // given
            Long boardTypeId = 1L;
            given(categoryRepository.findAllByBoardTypeId(boardTypeId)).willReturn(List.of());
            given(categoryMapper.toResponseList(List.of())).willReturn(List.of());

            // when
            List<CategoryResponse> result = categoryService.findAllByBoardType(boardTypeId);

            // then
            assertThat(result).isEmpty();
            verify(categoryRepository).findAllByBoardTypeId(boardTypeId);
            verify(categoryMapper).toResponseList(List.of());
        }
    }

    @Nested
    @DisplayName("ID로 카테고리 조회")
    class FindById {

        @Test
        @DisplayName("조회 성공")
        void findById_Success() {
            // given
            Long categoryId = 1L;
            CategoryVO category = createCategoryVO(categoryId, "카테고리1");
            CategoryResponse expectedResponse = createCategoryResponse(categoryId, "카테고리1");

            given(categoryRepository.findById(categoryId)).willReturn(Optional.of(category));
            given(categoryMapper.toResponse(category)).willReturn(expectedResponse);

            // when
            CategoryResponse result = categoryService.findById(categoryId);

            // then
            assertThat(result.id()).isEqualTo(categoryId);
            assertThat(result.name()).isEqualTo("카테고리1");
            verify(categoryRepository).findById(categoryId);
            verify(categoryMapper).toResponse(category);
        }

        @Test
        @DisplayName("존재하지 않는 카테고리 조회시 실패")
        void findById_NotFound_Fail() {
            // given
            Long categoryId = 999L;
            given(categoryRepository.findById(categoryId)).willReturn(Optional.empty());

            // when & then
            CustomException exception = assertThrows(CustomException.class,
                    () -> categoryService.findById(categoryId));

            assertThat(exception.getErrorCode()).isEqualTo(BoardErrorCode.CATEGORY_NOT_FOUND);
            verify(categoryRepository).findById(categoryId);
        }
    }

    private CategoryVO createCategoryVO(Long id, String name) {
        return CategoryVO.builder()
                .id(id)
                .name(name)
                .build();
    }

    private CategoryResponse createCategoryResponse(Long id, String name) {
        return new CategoryResponse(id, name);
    }
}