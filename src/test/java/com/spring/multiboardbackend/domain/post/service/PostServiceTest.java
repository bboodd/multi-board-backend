package com.spring.multiboardbackend.domain.post.service;

import com.spring.multiboardbackend.domain.post.exception.PostErrorCode;
import com.spring.multiboardbackend.domain.post.repository.PostRepository;
import com.spring.multiboardbackend.domain.post.vo.PostVO;
import com.spring.multiboardbackend.global.common.vo.SearchVO;
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
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("게시글 Service 테스트")
class PostServiceTest {

    @InjectMocks
    private PostService postService;

    @Mock
    private PostRepository postRepository;

    @Nested
    @DisplayName("게시글 등록")
    class Save {

        @Test
        @DisplayName("게시글 등록 성공")
        void save_Success() {
            // given
            PostVO post = createPostVO();

            // when
            PostVO result = postService.save(post);

            // then
            assertThat(result).isNotNull();
            assertThat(result)
                    .extracting(
                            "id",
                            "title",
                            "content"
                    )
                    .containsExactly(
                            1L,
                            "테스트 제목",
                            "테스트 내용"
                    );

            verify(postRepository).save(post);
        }
    }

    @Nested
    @DisplayName("게시글 조회")
    class Find {

        @Test
        @DisplayName("ID로 게시글 상세 조회 성공")
        void findByIdWithDetail_Success() {
            // given
            Long postId = 1L;
            PostVO post = createPostVO();

            given(postRepository.findByIdWithDetail(postId)).willReturn(Optional.of(post));

            // when
            PostVO result = postService.findByIdWithDetail(postId);

            // then
            assertThat(result)
                    .extracting(
                            "id",
                            "title",
                            "content"
                    )
                    .containsExactly(
                            1L,
                            "테스트 제목",
                            "테스트 내용"
                    );
        }

        @Test
        @DisplayName("ID로 게시글 조회 성공")
        void findById_Success() {
            // given
            Long postId = 1L;
            PostVO post = createPostVO();

            given(postRepository.findById(postId)).willReturn(Optional.of(post));

            // when
            PostVO result = postService.findById(postId);

            // then
            assertThat(result)
                    .extracting(
                            "id",
                            "title",
                            "content"
                    )
                    .containsExactly(
                            1L,
                            "테스트 제목",
                            "테스트 내용"
                    );
        }

        @Test
        @DisplayName("존재하지 않는 게시글 조회시 예외 발생")
        void findById_NotFound_ThrowException() {
            // given
            Long postId = 999L;
            given(postRepository.findById(postId)).willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> postService.findById(postId))
                    .isInstanceOf(CustomException.class)
                    .hasFieldOrPropertyWithValue("errorCode", PostErrorCode.POST_NOT_FOUND);
        }
    }

    @Nested
    @DisplayName("게시글 수정")
    class Update {

        @Test
        @DisplayName("게시글 수정 성공")
        void update_Success() {
            // given
            PostVO post = createUpdatedPostVO();

            // when
            PostVO result = postService.update(post);

            // then
            assertThat(result)
                    .extracting(
                            "id",
                            "title",
                            "content"
                    )
                    .containsExactly(
                            1L,
                            "수정된 제목",
                            "수정된 내용"
                    );

            verify(postRepository).update(post);
        }
    }

    @Nested
    @DisplayName("게시글 삭제")
    class Delete {

        @Test
        @DisplayName("게시글 삭제 성공")
        void delete_Success() {
            // given
            Long postId = 1L;

            // when
            boolean result = postService.delete(postId);

            // then
            assertThat(result).isTrue();
            verify(postRepository).deleteById(postId);
        }
    }

    @Nested
    @DisplayName("게시글 목록 조회")
    class FindAll {

        @Test
        @DisplayName("검색 조건으로 게시글 목록 조회 성공")
        void findAll_Success() {
            // given
            SearchVO search = SearchVO.builder()
                    .page(1)
                    .size(10)
                    .build();

            List<PostVO> posts = List.of(createPostVO());

            given(postRepository.findAllBySearch(search)).willReturn(posts);

            // when
            List<PostVO> result = postService.findAll(search);

            // then
            assertThat(result)
                    .hasSize(1)
                    .first()
                    .satisfies(post -> {
                        assertThat(post)
                                .extracting(
                                        "id",
                                        "title",
                                        "content"
                                )
                                .containsExactly(
                                        1L,
                                        "테스트 제목",
                                        "테스트 내용"
                                );
                    });
        }
    }

    @Nested
    @DisplayName("조회수 증가")
    class IncrementViewCount {

        @Test
        @DisplayName("게시글 조회수 증가 성공")
        void incrementViewCount_Success() {
            // given
            Long postId = 1L;
            PostVO post = createPostVO();

            given(postRepository.findById(postId)).willReturn(Optional.of(post));

            // when
            postService.incrementViewCount(postId);

            // then
            verify(postRepository).incrementViewCount(postId);
        }

        @Test
        @DisplayName("존재하지 않는 게시글의 조회수 증가 시도시 예외 발생")
        void incrementViewCount_NotFound_ThrowException() {
            // given
            Long postId = 999L;
            given(postRepository.findById(postId)).willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> postService.incrementViewCount(postId))
                    .isInstanceOf(CustomException.class)
                    .hasFieldOrPropertyWithValue("errorCode", PostErrorCode.POST_NOT_FOUND);
        }
    }

    // Helper Methods
    private PostVO createPostVO() {
        return PostVO.builder()
                .id(1L)
                .title("테스트 제목")
                .content("테스트 내용")
                .memberId(1L)
                .categoryId(1L)
                .build();
    }

    private PostVO createUpdatedPostVO() {
        return PostVO.builder()
                .id(1L)
                .title("수정된 제목")
                .content("수정된 내용")
                .memberId(1L)
                .categoryId(1L)
                .build();
    }
}