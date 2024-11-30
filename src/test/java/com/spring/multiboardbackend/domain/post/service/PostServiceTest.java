package com.spring.multiboardbackend.domain.post.service;

import com.spring.multiboardbackend.domain.board.enums.BoardType;
import com.spring.multiboardbackend.domain.member.service.AuthService;
import com.spring.multiboardbackend.domain.post.dto.request.PostRequest;
import com.spring.multiboardbackend.domain.post.dto.response.CommentResponse;
import com.spring.multiboardbackend.domain.post.dto.response.FileResponse;
import com.spring.multiboardbackend.domain.post.dto.response.PostResponse;
import com.spring.multiboardbackend.domain.post.dto.response.PostsResponse;
import com.spring.multiboardbackend.domain.post.exception.PostErrorCode;
import com.spring.multiboardbackend.domain.post.mapper.PostMapper;
import com.spring.multiboardbackend.domain.post.repository.PostRepository;
import com.spring.multiboardbackend.domain.post.vo.PostVO;
import com.spring.multiboardbackend.global.common.mapper.SearchMapper;
import com.spring.multiboardbackend.global.common.request.SearchRequest;
import com.spring.multiboardbackend.global.common.vo.SearchVO;
import com.spring.multiboardbackend.global.exception.CustomException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("게시글 Service 테스트")
class PostServiceTest {

    @InjectMocks
    private PostService postService;

    @Mock
    private PostRepository postRepository;

    @Mock
    private PostMapper postMapper;

    @Mock
    private SearchMapper searchMapper;

    @Mock
    private AuthService authService;

    @Mock
    private FileService fileService;

    @Nested
    @DisplayName("게시글 등록")
    class Save {

        @Test
        @DisplayName("일반 게시글 등록 성공")
        void save_Normal_Success() {
            // given
            PostRequest request = createPostRequest();
            Long memberId = 1L;
            BoardType type = BoardType.FREE;

            PostVO postVO = createPostVO();
            PostResponse expectedResponse = createPostResponse();

            given(postMapper.toVO(request, memberId, type.getId())).willReturn(postVO);
            given(fileService.saveFiles(request.files(), postVO.getId(), type)).willReturn(List.of());
            given(postMapper.toResponseForCreateAndUpdate(postVO, List.of())).willReturn(expectedResponse);

            // when
            PostResponse result = postService.save(request, memberId, type);

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

            assertThat(result.locked()).isFalse();
            assertThat(result.files()).isEmpty();
            assertThat(result.comments()).isEmpty();

            verify(postRepository).save(postVO);
            verify(fileService).saveFiles(request.files(), postVO.getId(), type);
        }

        @Test
        @DisplayName("파일 첨부 게시글 등록 성공")
        void save_WithFile_Success() {
            // given
            PostRequest request = createPostRequestWithFiles();
            Long memberId = 1L;
            BoardType type = BoardType.FREE;

            PostVO postVO = createPostVO();
            List<FileResponse> fileResponses = List.of(createFileResponse());
            PostResponse expectedResponse = createPostResponseWithFiles();

            given(postMapper.toVO(request, memberId, type.getId())).willReturn(postVO);
            given(fileService.saveFiles(request.files(), postVO.getId(), type)).willReturn(fileResponses);
            given(postMapper.toResponseForCreateAndUpdate(postVO, fileResponses)).willReturn(expectedResponse);

            // when
            PostResponse result = postService.save(request, memberId, type);

            // then
            assertThat(result)
                    .extracting(
                            "id",
                            "title",
                            "content",
                            "categoryName",
                            "nickname",
                            "fileCount"
                    )
                    .containsExactly(
                            1L,
                            "테스트 제목",
                            "테스트 내용",
                            "자유게시판",
                            "테스트유저",
                            1
                    );

            assertThat(result.files())
                    .hasSize(1)
                    .first()
                    .satisfies(file -> {
                        assertThat(file)
                                .extracting(
                                        "id",
                                        "originalName",
                                        "fileSize"
                                )
                                .containsExactly(
                                        1L,
                                        "test.txt",
                                        1024L
                                );
                    });

            verify(postRepository).save(postVO);
        }
    }

    @Nested
    @DisplayName("게시글 목록 조회")
    class FindAll {

        @Test
        @DisplayName("게시글 목록 조회 성공")
        void findAll_Success() {
            // given
            SearchRequest searchRequest = SearchRequest.builder()
                    .page(1)
                    .size(10)
                    .build();
            BoardType type = BoardType.FREE;

            SearchVO searchVO = SearchVO.builder()
                    .page(1)
                    .size(10)
                    .boardTypeId(type.getId())
                    .build();

            List<PostVO> posts = List.of(createPostVO());
            List<PostResponse> expectedResponses = List.of(createPostResponse());

            given(searchMapper.toVO(searchRequest, type.getId())).willReturn(searchVO);
            given(postRepository.countBySearch(searchVO)).willReturn(15); // 총 15개 데이터
            given(postRepository.findAllBySearch(searchVO)).willReturn(posts);
            given(postMapper.toResponse(any(PostVO.class))).willReturn(expectedResponses.get(0));

            // when
            PostsResponse result = postService.findAll(searchRequest, type);

            // then
            assertThat(result.data())
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

            // Pagination 검증
            assertThat(result.pagination())
                    .extracting(
                            "currentPage",
                            "totalCount",
                            "hasMore"
                    )
                    .containsExactly(
                            1,      // currentPage
                            15,     // totalCount
                            true    // hasMore (1페이지*10개 < 총15개이므로 true)
                    );
        }

        @Test
        @DisplayName("마지막 페이지 조회 성공")
        void findAll_LastPage_Success() {
            // given
            SearchRequest searchRequest = SearchRequest.builder()
                    .page(2)
                    .size(10)
                    .build();
            BoardType type = BoardType.FREE;

            SearchVO searchVO = SearchVO.builder()
                    .page(2)
                    .size(10)
                    .boardTypeId(type.getId())
                    .build();

            List<PostVO> posts = List.of(createPostVO());
            List<PostResponse> expectedResponses = List.of(createPostResponse());

            given(searchMapper.toVO(searchRequest, type.getId())).willReturn(searchVO);
            given(postRepository.countBySearch(searchVO)).willReturn(15); // 총 15개 데이터
            given(postRepository.findAllBySearch(searchVO)).willReturn(posts);
            given(postMapper.toResponse(any(PostVO.class))).willReturn(expectedResponses.get(0));

            // when
            PostsResponse result = postService.findAll(searchRequest, type);

            // then
            assertThat(result.pagination())
                    .extracting(
                            "currentPage",
                            "totalCount",
                            "hasMore"
                    )
                    .containsExactly(
                            2,      // currentPage
                            15,     // totalCount
                            false   // hasMore (2페이지*10개 >= 총15개이므로 false)
                    );
        }

        @Test
        @DisplayName("검색 결과가 없을 경우 빈 페이지네이션 반환")
        void findAll_NoResults_ReturnEmpty() {
            // given
            SearchRequest searchRequest = SearchRequest.builder().build();
            BoardType type = BoardType.FREE;

            SearchVO searchVO = SearchVO.builder()
                    .page(1)
                    .size(10)
                    .boardTypeId(type.getId())
                    .build();

            given(searchMapper.toVO(searchRequest, type.getId())).willReturn(searchVO);
            given(postRepository.countBySearch(searchVO)).willReturn(0);

            // when
            PostsResponse result = postService.findAll(searchRequest, type);

            // then
            assertThat(result.data()).isEmpty();
            assertThat(result.pagination())
                    .extracting(
                            "currentPage",
                            "totalCount",
                            "hasMore"
                    )
                    .containsExactly(
                            1,      // currentPage
                            0,      // totalCount
                            false   // hasMore
                    );
        }
    }

    @Nested
    @DisplayName("게시글 상세 조회")
    class FindDetail {

        @Test
        @DisplayName("게시글 상세 조회 성공")
        void findDetail_Success() {
            // given
            Long postId = 1L;
            PostVO postVO = createPostVOWithDetails();
            PostResponse expectedResponse = createPostResponseWithDetails();

            given(postRepository.findByIdWithDetail(postId)).willReturn(Optional.of(postVO));
            given(postMapper.toResponse(postVO)).willReturn(expectedResponse);

            // when
            PostResponse result = postService.findById(postId);

            // then
            assertThat(result)
                    .extracting(
                            "id",
                            "title",
                            "content",
                            "viewCount",
                            "categoryName",
                            "nickname",
                            "fileCount",
                            "commentCount"
                    )
                    .containsExactly(
                            1L,
                            "테스트 제목",
                            "테스트 내용",
                            10,
                            "자유게시판",
                            "테스트유저",
                            2,
                            3
                    );

            // 파일 검증
            assertThat(result.files())
                    .hasSize(2)
                    .first()
                    .satisfies(file -> {
                        assertThat(file)
                                .extracting(
                                        "id",
                                        "originalName"
                                )
                                .containsExactly(
                                        1L,
                                        "test1.txt"
                                );
                    });

            // 댓글 검증
            assertThat(result.comments())
                    .hasSize(3)
                    .first()
                    .satisfies(comment -> {
                        assertThat(comment)
                                .extracting(
                                        "id",
                                        "content",
                                        "nickname"
                                )
                                .containsExactly(
                                        1L,
                                        "테스트 댓글 1",
                                        "댓글작성자1"
                                );
                    });
        }

        @Test
        @DisplayName("존재하지 않는 게시글 상세 조회시 실패")
        void findDetail_NotFound_Fail() {
            // given
            Long postId = 999L;
            given(postRepository.findByIdWithDetail(postId)).willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> postService.findById(postId))
                    .isInstanceOf(CustomException.class)
                    .hasFieldOrPropertyWithValue("errorCode", PostErrorCode.POST_NOT_FOUND);
        }

        private PostVO createPostVOWithDetails() {
            return PostVO.builder()
                    .id(1L)
                    .memberId(1L)
                    .categoryId(1L)
                    .title("테스트 제목")
                    .content("테스트 내용")
                    .viewCount(10)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .deleted(false)
                    .locked(false)
                    .build();
        }

        private PostResponse createPostResponseWithDetails() {
            return new PostResponse(
                    1L,                                    // id
                    "테스트 제목",                           // title
                    "테스트 내용",                           // content
                    10,                                    // viewCount
                    LocalDateTime.now(),                   // createdAt
                    "자유게시판",                            // categoryName
                    "테스트유저",                            // nickname
                    2,                                     // fileCount
                    3,                                     // commentCount
                    null,                                  // thumbnailUrl
                    false,                                 // locked
                    createDetailFileResponses(),           // files
                    createDetailCommentResponses()         // comments
            );
        }

        private List<FileResponse> createDetailFileResponses() {
            return List.of(
                    new FileResponse(
                            1L,
                            "test1.txt",
                            1024L
                    ),
                    new FileResponse(
                            2L,
                            "test2.txt",
                            2048L
                    )
            );
        }

        private List<CommentResponse> createDetailCommentResponses() {
            return List.of(
                    new CommentResponse(
                            1L,                    // id
                            "테스트 댓글 1",          // content
                            LocalDateTime.now(),    // createdAt
                            "댓글작성자1"            // nickname
                    ),
                    new CommentResponse(
                            2L,
                            "테스트 댓글 2",
                            LocalDateTime.now(),
                            "댓글작성자2"
                    ),
                    new CommentResponse(
                            3L,
                            "테스트 댓글 3",
                            LocalDateTime.now(),
                            "댓글작성자3"
                    )
            );
        }
    }

    @Nested
    @DisplayName("게시글 수정")
    class Update {

        @Test
        @DisplayName("일반 게시글 수정 성공 (파일 없음)")
        void update_WithoutFile_Success() {
            // given
            PostRequest request = createPostRequest();
            Long memberId = 1L;
            Long postId = 1L;
            BoardType type = BoardType.FREE;

            PostVO existingPost = createPostVO();
            PostVO updatedPost = createUpdatedPostVO();
            PostResponse expectedResponse = createUpdatedPostResponse();

            given(postRepository.findById(postId)).willReturn(Optional.of(existingPost));
            given(postMapper.toVOForUpdate(request, postId)).willReturn(updatedPost);
            given(fileService.updateFiles(request.removeFileIds(), request.files(), postId, type))
                    .willReturn(List.of());
            given(postMapper.toResponseForCreateAndUpdate(updatedPost, List.of()))
                    .willReturn(expectedResponse);

            // when
            PostResponse result = postService.update(request, memberId, postId, type);

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

            assertThat(result.files()).isEmpty();
            verify(postRepository).update(updatedPost);
        }

        @Test
        @DisplayName("게시글 수정 성공 (파일 추가)")
        void update_WithNewFiles_Success() {
            // given
            PostRequest request = createPostRequestWithFiles();
            Long memberId = 1L;
            Long postId = 1L;
            BoardType type = BoardType.FREE;

            PostVO existingPost = createPostVO();
            PostVO updatedPost = createUpdatedPostVO();
            List<FileResponse> updatedFiles = List.of(
                    createFileResponse(1L, "newfile1.txt"),
                    createFileResponse(2L, "newfile2.txt")
            );
            PostResponse expectedResponse = createUpdatedPostResponseWithFiles(updatedFiles);

            given(postRepository.findById(postId)).willReturn(Optional.of(existingPost));
            given(postMapper.toVOForUpdate(request, postId)).willReturn(updatedPost);
            given(fileService.updateFiles(request.removeFileIds(), request.files(), postId, type))
                    .willReturn(updatedFiles);
            given(postMapper.toResponseForCreateAndUpdate(updatedPost, updatedFiles))
                    .willReturn(expectedResponse);

            // when
            PostResponse result = postService.update(request, memberId, postId, type);

            // then
            assertThat(result)
                    .extracting(
                            "id",
                            "title",
                            "content",
                            "fileCount"
                    )
                    .containsExactly(
                            1L,
                            "수정된 제목",
                            "수정된 내용",
                            2
                    );

            assertThat(result.files())
                    .hasSize(2)
                    .extracting("originalName")
                    .containsExactly("newfile1.txt", "newfile2.txt");

            verify(postRepository).update(updatedPost);
            verify(fileService).updateFiles(request.removeFileIds(), request.files(), postId, type);
        }

        @Test
        @DisplayName("게시글 수정 성공 (파일 삭제)")
        void update_RemoveFiles_Success() {
            // given
            PostRequest request = PostRequest.builder()
                    .categoryId(1L)
                    .title("수정된 제목")
                    .content("수정된 내용")
                    .removeFileIds(List.of(1L, 2L))
                    .build();

            Long memberId = 1L;
            Long postId = 1L;
            BoardType type = BoardType.FREE;

            PostVO existingPost = createPostVO();
            PostVO updatedPost = createUpdatedPostVO();
            PostResponse expectedResponse = createUpdatedPostResponse();

            given(postRepository.findById(postId)).willReturn(Optional.of(existingPost));
            given(postMapper.toVOForUpdate(request, postId)).willReturn(updatedPost);
            given(fileService.updateFiles(request.removeFileIds(), request.files(), postId, type))
                    .willReturn(List.of());
            given(postMapper.toResponseForCreateAndUpdate(updatedPost, List.of()))
                    .willReturn(expectedResponse);

            // when
            PostResponse result = postService.update(request, memberId, postId, type);

            // then
            assertThat(result.files()).isEmpty();
            assertThat(result.fileCount()).isZero();

            verify(fileService).updateFiles(
                    argThat(removeIds -> removeIds.containsAll(List.of(1L, 2L))),
                    any(),
                    eq(postId),
                    eq(type)
            );
        }

        // Helper Methods
        private FileResponse createFileResponse(Long id, String originalName) {
            return new FileResponse(
                    id,
                    originalName,
                    1024L
            );
        }

        private PostResponse createUpdatedPostResponseWithFiles(List<FileResponse> files) {
            return new PostResponse(
                    1L,                    // id
                    "수정된 제목",           // title
                    "수정된 내용",           // content
                    0,                     // viewCount
                    LocalDateTime.now(),   // createdAt
                    "자유게시판",           // categoryName
                    "테스트유저",           // nickname
                    files.size(),          // fileCount
                    0,                     // commentCount
                    null,                  // thumbnailUrl
                    false,                 // locked
                    files,                 // files
                    List.of()              // comments
            );
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
            Long memberId = 1L;
            PostVO post = createPostVO();

            given(postRepository.findById(postId)).willReturn(Optional.of(post));

            // when
            boolean result = postService.delete(postId, memberId);

            // then
            assertThat(result).isTrue();
            verify(postRepository).deleteById(postId);
        }

        @Test
        @DisplayName("관리자 권한으로 게시글 삭제 성공")
        void delete_AsAdmin_Success() {
            // given
            Long postId = 1L;
            Long adminId = 2L;
            PostVO post = createPostVO(); // memberId가 1L인 게시글

            given(postRepository.findById(postId)).willReturn(Optional.of(post));
            given(authService.isAdmin(adminId)).willReturn(true);

            // when
            boolean result = postService.delete(postId, adminId);

            // then
            assertThat(result).isTrue();
            verify(postRepository).deleteById(postId);
        }

        @Test
        @DisplayName("권한 없는 사용자의 삭제 시도시 실패")
        void delete_NoPermission_Fail() {
            // given
            Long postId = 1L;
            Long unauthorizedMemberId = 2L;
            PostVO post = createPostVO();

            given(postRepository.findById(postId)).willReturn(Optional.of(post));
            given(authService.isAdmin(unauthorizedMemberId)).willReturn(false);

            // when & then
            assertThatThrownBy(() -> postService.delete(postId, unauthorizedMemberId))
                    .isInstanceOf(CustomException.class)
                    .hasFieldOrPropertyWithValue("errorCode", PostErrorCode.PERMISSION_DENIED);
        }

        @Test
        @DisplayName("존재하지 않는 게시글 삭제 시도시 실패")
        void delete_NotFound_Fail() {
            // given
            Long postId = 999L;
            Long memberId = 1L;

            given(postRepository.findById(postId)).willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> postService.delete(postId, memberId))
                    .isInstanceOf(CustomException.class)
                    .hasFieldOrPropertyWithValue("errorCode", PostErrorCode.POST_NOT_FOUND);
        }
    }


    // Helper Methods
    private PostRequest createPostRequest() {
        return PostRequest.builder()
                .categoryId(1L)
                .title("테스트 제목")
                .content("테스트 내용")
                .build();
    }

    private PostRequest createPostRequestWithFiles() {
        return PostRequest.builder()
                .categoryId(1L)
                .title("테스트 제목")
                .content("테스트 내용")
                .files(List.of(new MockMultipartFile("file", "test.txt", "text/plain", "test".getBytes())))
                .build();
    }

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

    private PostResponse createPostResponse() {
        return new PostResponse(
                1L,                                    // id
                "테스트 제목",                           // title
                "테스트 내용",                           // content
                0,                                     // viewCount
                LocalDateTime.now(),                   // createdAt
                "자유게시판",                            // categoryName
                "테스트유저",                            // nickname
                0,                                     // fileCount
                0,                                     // commentCount
                null,                                  // thumbnailUrl
                false,                                 // locked
                List.of(),                            // files
                List.of()                             // comments
        );
    }

    private PostResponse createPostResponseWithFiles() {
        return new PostResponse(
                1L,                                    // id
                "테스트 제목",                           // title
                "테스트 내용",                           // content
                0,                                     // viewCount
                LocalDateTime.now(),                   // createdAt
                "자유게시판",                            // categoryName
                "테스트유저",                            // nickname
                1,                                     // fileCount
                0,                                     // commentCount
                "/thumbnail/1",                        // thumbnailUrl
                false,                                 // locked
                List.of(createFileResponse()),         // files
                List.of()                             // comments
        );
    }



    private PostResponse createUpdatedPostResponse() {
        return new PostResponse(
                1L,                                    // id
                "수정된 제목",                           // title
                "수정된 내용",                           // content
                0,                                     // viewCount
                LocalDateTime.now(),                   // createdAt
                "자유게시판",                            // categoryName
                "테스트유저",                            // nickname
                0,                                     // fileCount
                0,                                     // commentCount
                null,                                  // thumbnailUrl
                false,                                 // locked
                List.of(),                            // files
                List.of()                             // comments
        );
    }

    private FileResponse createFileResponse() {
        return new FileResponse(
                1L,                        // id
                "test.txt",                // originalName
                1024L                     // fileSize
        );
    }
}