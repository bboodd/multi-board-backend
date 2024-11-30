package com.spring.multiboardbackend.domain.post.repository;

import com.spring.multiboardbackend.domain.post.vo.PostVO;
import com.spring.multiboardbackend.global.common.vo.SearchVO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DisplayName("게시글 Repository 테스트")
@Sql("/sql/post-test-data.sql")
class PostRepositoryTest {

    @Autowired
    private PostRepository postRepository;

    @Nested
    @DisplayName("게시글 저장")
    class Save {

        @Test
        @DisplayName("게시글 저장 성공")
        void save_Success() {
            // given
            PostVO post = PostVO.builder()
                    .memberId(1L)
                    .boardTypeId(1L)
                    .categoryId(1L)
                    .title("새 게시글")
                    .content("새 내용")
                    .locked(false)
                    .fixed(false)
                    .build();

            // when
            postRepository.save(post);

            // then
            assertThat(post.getId()).isNotNull();

            Optional<PostVO> saved = postRepository.findById(post.getId());
            assertThat(saved).isPresent()
                    .get()
                    .satisfies(p -> {
                        assertThat(p)
                                .extracting(
                                        "memberId",
                                        "categoryId",
                                        "title",
                                        "content",
                                        "viewCount",
                                        "deleted",
                                        "locked",
                                        "fixed"
                                )
                                .containsExactly(
                                        1L,
                                        1L,
                                        "새 게시글",
                                        "새 내용",
                                        0,
                                        false,
                                        false,
                                        false
                                );
                    });
        }
    }

    @Nested
    @DisplayName("게시글 조회")
    class Find {

        @Test
        @DisplayName("ID로 게시글 조회 성공")
        void findById_Success() {
            // given
            Long postId = 1L;

            // when
            Optional<PostVO> result = postRepository.findById(postId);

            // then
            assertThat(result).isPresent()
                    .get()
                    .satisfies(post -> {
                        assertThat(post)
                                .extracting(
                                        "id",
                                        "title",
                                        "content",
                                        "nickname",
                                        "categoryName"
                                )
                                .containsExactly(
                                        1L,
                                        "테스트 게시글 1",
                                        "내용 1",
                                        "테스트유저",
                                        "일반"
                                );
                    });
        }

        @Test
        @DisplayName("삭제된 게시글 조회시 빈 Optional 반환")
        void findById_Deleted_ReturnEmpty() {
            // given
            Long postId = 1L;
            postRepository.deleteById(postId);

            // when
            Optional<PostVO> result = postRepository.findById(postId);

            // then
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("게시글 상세 조회 성공 (파일, 댓글 포함)")
        void findByIdWithDetail_Success() {
            // given
            Long postId = 1L;

            // when
            Optional<PostVO> result = postRepository.findByIdWithDetail(postId);

            // then
            assertThat(result).isPresent()
                    .get()
                    .satisfies(post -> {
                        // 게시글 기본 정보 검증
                        assertThat(post)
                                .extracting(
                                        "id",
                                        "title",
                                        "nickname",
                                        "categoryName"
                                )
                                .containsExactly(
                                        1L,
                                        "테스트 게시글 1",
                                        "테스트유저",
                                        "일반"
                                );

                        // 파일 정보 검증
                        assertThat(post.getFiles())
                                .hasSize(1)
                                .first()
                                .matches(file ->
                                        file.getOriginalName().equals("test1.txt") &&
                                                file.getContentType().equals("text/plain")
                                );

                        // 댓글 정보 검증
                        assertThat(post.getComments())
                                .hasSize(1)
                                .first()
                                .satisfies(comment ->
                                        assertThat(comment)
                                                .extracting(
                                                        "content",
                                                        "nickname",
                                                        "deleted"
                                                )
                                                .containsExactly(
                                                        "테스트 댓글 1",
                                                        "테스트유저",
                                                        false
                                                )
                                );
                    });
        }
    }

    @Nested
    @DisplayName("게시글 수정")
    class Update {

        @Test
        @DisplayName("게시글 수정 성공")
        void update_Success() {
            // given
            Long postId = 1L;
            PostVO updatePost = PostVO.builder()
                    .id(postId)
                    .categoryId(2L)
                    .title("수정된 제목")
                    .content("수정된 내용")
                    .locked(true)
                    .build();

            // when
            postRepository.update(updatePost);

            // then
            Optional<PostVO> updated = postRepository.findById(postId);
            assertThat(updated).isPresent()
                    .get()
                    .extracting(
                            "categoryId",
                            "title",
                            "content",
                            "locked"
                    )
                    .containsExactly(
                            2L,
                            "수정된 제목",
                            "수정된 내용",
                            true
                    );
        }
    }

    @Nested
    @DisplayName("게시글 삭제")
    class Delete {

        @Test
        @DisplayName("게시글 삭제 성공 (소프트 딜리트)")
        void delete_Success() {
            // given
            Long postId = 1L;

            // when
            postRepository.deleteById(postId);

            // then
            Optional<PostVO> deleted = postRepository.findByIdWithDeleted(postId);
            assertThat(deleted).isPresent()
                    .get()
                    .extracting(
                            "id",
                            "deleted",
                            "title"
                    )
                    .containsExactly(
                            1L,
                            true,
                            "테스트 게시글 1"
                    );

            // 일반 조회로는 조회되지 않는지 확인
            Optional<PostVO> notFound = postRepository.findById(postId);
            assertThat(notFound).isEmpty();
        }
    }

    @Nested
    @DisplayName("게시글 검색")
    class Search {

        @Test
        @DisplayName("검색 조건으로 목록 조회 성공")
        void findAllBySearch_Success() {
            // given
            SearchVO search = SearchVO.builder()
                    .boardTypeId(1L)      // 자유게시판
                    .categoryId(1L)   // 일반
                    .page(1)
                    .size(10)
                    .build();

            // when
            List<PostVO> results = postRepository.findAllBySearch(search);

            // then
            assertThat(results)
                    .isNotEmpty()
                    .hasSize(2)
                    .allSatisfy(post -> {
                        assertThat(post.getCategoryId()).isEqualTo(1L);
                        assertThat(post.isDeleted()).isFalse();
                    });
        }

        @Test
        @DisplayName("키워드로 검색 성공")
        void findAllBySearch_WithKeyword_Success() {
            // given
            SearchVO search = SearchVO.builder()
                    .boardTypeId(1L)
                    .keyword("테스트")
                    .page(1)
                    .size(10)
                    .build();

            // when
            List<PostVO> results = postRepository.findAllBySearch(search);

            // then
            assertThat(results)
                    .isNotEmpty()
                    .allSatisfy(post ->
                            assertThat(post.getTitle()).contains("테스트")
                    );
        }

        @Test
        @DisplayName("작성자로 검색 성공")
        void findAllBySearch_ByNickname_Success() {
            // given
            SearchVO search = SearchVO.builder()
                    .boardTypeId(1L)
                    .nickname("테스트유저")
                    .page(1)
                    .size(10)
                    .build();

            // when
            List<PostVO> results = postRepository.findAllBySearch(search);

            // then
            assertThat(results)
                    .isNotEmpty()
                    .allSatisfy(post ->
                            assertThat(post.getNickname()).isEqualTo("테스트유저")
                    );
        }
    }

    @Test
    @DisplayName("조회수 증가 성공")
    void incrementViewCount_Success() {
        // given
        Long postId = 1L;
        int initialViewCount = postRepository.findById(postId)
                .map(PostVO::getViewCount)
                .orElse(0);

        // when
        postRepository.incrementViewCount(postId);

        // then
        Optional<PostVO> updated = postRepository.findById(postId);
        assertThat(updated).isPresent()
                .get()
                .extracting("viewCount")
                .isEqualTo(initialViewCount + 1);
    }
}