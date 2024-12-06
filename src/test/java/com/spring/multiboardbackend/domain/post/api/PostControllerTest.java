package com.spring.multiboardbackend.domain.post.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.spring.multiboardbackend.domain.board.enums.BoardType;
import com.spring.multiboardbackend.domain.member.service.AuthService;
import com.spring.multiboardbackend.domain.post.dto.request.PostRequest;
import com.spring.multiboardbackend.domain.post.dto.response.FileResponse;
import com.spring.multiboardbackend.domain.post.dto.response.PostResponse;
import com.spring.multiboardbackend.domain.post.mapper.FileMapper;
import com.spring.multiboardbackend.domain.post.mapper.PostMapper;
import com.spring.multiboardbackend.domain.post.service.FileService;
import com.spring.multiboardbackend.domain.post.service.PostService;
import com.spring.multiboardbackend.domain.post.vo.PostVO;
import com.spring.multiboardbackend.global.common.mapper.SearchMapper;
import com.spring.multiboardbackend.global.common.request.SearchRequest;
import com.spring.multiboardbackend.global.common.vo.SearchVO;
import com.spring.multiboardbackend.global.security.auth.AuthenticationContextHolder;
import com.spring.multiboardbackend.global.util.FileUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("게시글 Controller 테스트")
class PostControllerTest {

    @InjectMocks
    private PostController postController;

    @Mock
    private PostService postService;

    @Mock
    private PostMapper postMapper;

    @Mock
    private SearchMapper searchMapper;

    @Mock
    private FileMapper fileMapper;

    @Mock
    private FileService fileService;

    @Mock
    private FileUtils fileUtils;

    @Mock
    private AuthService authService;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        mockMvc = MockMvcBuilders
                .standaloneSetup(postController)
                .build();
    }

    @Nested
    @DisplayName("게시글 목록 조회")
    class GetPosts {

        @Test
        @DisplayName("게시글 목록 조회 성공")
        void getPosts_Success() throws Exception {
            // given
            String boardType = "free";
            SearchVO searchVO = SearchVO.builder()
                    .page(1)
                    .size(10)
                    .boardTypeId(BoardType.FREE.getId())
                    .build();
            List<PostVO> posts = List.of(createPostVO());
            List<PostResponse> postResponses = List.of(createPostResponse());

            given(searchMapper.toVO(any(SearchRequest.class), eq(BoardType.FREE.getId()))).willReturn(searchVO);
            given(postService.countBySearch(searchVO)).willReturn(15);
            given(postService.findAll(searchVO)).willReturn(posts);
            given(postMapper.toResponseList(posts)).willReturn(postResponses);

            // when & then
            mockMvc.perform(get("/api/boards/{boardType}/posts", boardType)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data[0].title").value("테스트 게시글"))
                    .andExpect(jsonPath("$.pagination.totalCount").value(15));
        }
    }

    @Nested
    @DisplayName("게시글 상세 조회")
    class GetPost {

        @Test
        @DisplayName("게시글 상세 조회 성공")
        void getPost_Success() throws Exception {
            // given
            String boardType = "free";
            Long postId = 1L;
            PostVO post = createPostVO();
            PostResponse postResponse = createPostResponse();

            given(postService.findByIdWithDetail(postId)).willReturn(post);
            given(postMapper.toResponse(post)).willReturn(postResponse);

            // when & then
            mockMvc.perform(get("/api/boards/{boardType}/posts/{postId}", boardType, postId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.title").value("테스트 게시글"));

            verify(postService).incrementViewCount(postId);
        }
    }

    @Nested
    @DisplayName("게시글 작성")
    class SavePost {

        @Test
        @DisplayName("일반 게시글 작성 성공")
        void savePost_Normal_Success() throws Exception {
            // given
            String boardType = "notice";
            Long memberId = 1L;
            PostRequest request = createPostRequest();
            PostVO post = createNoticePostVO();
            PostResponse postResponse = createPostResponse();

            AuthenticationContextHolder.setContext(memberId);

            given(postMapper.toVO(any(PostRequest.class), eq(memberId), eq(BoardType.NOTICE.getId()))).willReturn(post);
            given(postService.save(any(PostVO.class))).willReturn(post);
            given(postMapper.toResponse(any(PostVO.class))).willReturn(postResponse);

            // when & then
            mockMvc.perform(post("/api/boards/{boardType}/posts", boardType)
                            .contentType(MediaType.MULTIPART_FORM_DATA)
                            .param("title", request.title())
                            .param("content", request.content())
                            .param("categoryId", String.valueOf(request.categoryId())))
                    .andDo(print())
                    .andExpect(status().isCreated())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.title").value("테스트 게시글"));
        }

        @Test
        @DisplayName("파일 첨부 게시글 작성 성공")
        void savePost_WithFile_Success() throws Exception {
            // given
            String boardType = "free";
            Long memberId = 1L;
            PostRequest request = createPostRequestWithFile();
            PostVO post = createPostVO();
            PostResponse postResponse = createPostResponseWithFile();
            MockMultipartFile file = new MockMultipartFile("file", "test.txt", MediaType.TEXT_PLAIN_VALUE, "test file".getBytes());

            AuthenticationContextHolder.setContext(memberId);

            given(postMapper.toVO(any(PostRequest.class), eq(memberId), eq(BoardType.FREE.getId()))).willReturn(post);
            given(postService.save(any(PostVO.class))).willReturn(post);
            given(postMapper.toResponseWithFile(any(PostVO.class), any())).willReturn(postResponse);

            // when & then
            mockMvc.perform(multipart("/api/boards/{boardType}/posts", boardType)
                            .file(file)
                            .param("title", request.title())
                            .param("content", request.content())
                            .param("categoryId", String.valueOf(request.categoryId()))
                            .with(servletRequest -> {
                                servletRequest.setMethod("POST");
                                return servletRequest;
                            }))
                    .andDo(print())
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.title").value("테스트 게시글"))
                    .andExpect(jsonPath("$.files[0].originalName").value("test.txt"));
        }
    }

    @Nested
    @DisplayName("게시글 수정")
    class UpdatePost {

        @Test
        @DisplayName("게시글 수정 성공")
        void updatePost_Success() throws Exception {
            // given
            String boardType = "free";
            Long postId = 1L;
            Long memberId = 1L;
            PostRequest request = createPostRequest();
            PostVO existingPost = createPostVO();
            PostVO updatedPost = createUpdatedPostVO();
            PostResponse postResponse = createUpdatedPostResponse();
            MockMultipartFile file = new MockMultipartFile("file", "test.txt", MediaType.TEXT_PLAIN_VALUE, "test file".getBytes());

            AuthenticationContextHolder.setContext(memberId);

            given(postService.findById(any())).willReturn(existingPost);
            given(postMapper.toVOForUpdate(any(), eq(postId))).willReturn(updatedPost);
            given(postService.update(any())).willReturn(updatedPost);
            given(postMapper.toResponseWithFile(any(), any())).willReturn(postResponse);

            // when & then
            mockMvc.perform(multipart("/api/boards/{boardType}/posts/{postId}", boardType, postId)
                            .file(file)
                            .param("title", request.title())
                            .param("content", request.content())
                            .param("categoryId", String.valueOf(request.categoryId()))
                            .with(servletRequest -> {
                                servletRequest.setMethod("PUT");
                                return servletRequest;
                            }))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.title").value("수정된 제목"));
        }
    }

    @Nested
    @DisplayName("게시글 삭제")
    class DeletePost {

        @Test
        @DisplayName("게시글 삭제 성공")
        void deletePost_Success() throws Exception {
            // given
            String boardType = "free";
            Long postId = 1L;
            Long memberId = 1L;
            PostVO post = createPostVO();

            AuthenticationContextHolder.setContext(memberId);

            given(postService.findById(postId)).willReturn(post);
            given(postService.delete(postId)).willReturn(true);

            // when & then
            mockMvc.perform(delete("/api/boards/{boardType}/posts/{postId}", boardType, postId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").value(true));
        }
    }

    // Helper Methods
    private PostVO createPostVO() {
        return PostVO.builder()
                .id(1L)
                .categoryId(1L)
                .title("테스트 게시글")
                .content("테스트 내용")
                .memberId(1L)
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

    private PostVO createNoticePostVO() {
        return PostVO.builder()
                .id(1L)
                .title("수정된 제목")
                .content("수정된 내용")
                .memberId(1L)
                .categoryId(1L)
                .boardTypeId(3L)
                .build();
    }

    private PostRequest createPostRequest() {
        return PostRequest.builder()
                .categoryId(1L)
                .title("테스트 게시글")
                .content("테스트 내용")
                .build();
    }

    private PostRequest createPostRequestWithFile() {
        return PostRequest.builder()
                .categoryId(1L)
                .title("테스트 게시글")
                .content("테스트 내용")
                .files(List.of(new MockMultipartFile("file", "test.txt", MediaType.TEXT_PLAIN_VALUE, "test".getBytes())))
                .build();
    }

    private PostResponse createPostResponse() {
        return new PostResponse(
                1L,                // categoryId
                "테스트 게시글",          // title
                "테스트 내용",           // content
                0,                     // viewCount
                LocalDateTime.now(),   // createdAt
                1L,
                "자유",                 // categoryName
                "테스트유저",             // nickname
                0,                     // fileCount
                0,                     // commentCount
                false,                 // locked
                false,
                List.of(),             // files
                List.of()              // comments
        );
    }

    private PostResponse createUpdatedPostResponse() {
        return new PostResponse(
                1L,                                    // id
                "수정된 제목",                           // title
                "수정된 내용",                           // content
                0,                                     // viewCount
                LocalDateTime.now(),                   // createdAt
                1L,
                "자유게시판",                            // categoryName
                "테스트유저",                            // nickname
                0,                                     // fileCount
                0,                                     // commentCount
                false,                                 // locked
                false,
                List.of(),                            // files
                List.of()                             // comments
        );
    }

    private PostResponse createPostResponseWithFile() {
        return new PostResponse(
                1L,                // categoryId
                "테스트 게시글",          // title
                "테스트 내용",           // content
                0,                     // viewCount
                LocalDateTime.now(),   // createdAt
                1L,
                "자유",                 // categoryName
                "테스트유저",             // nickname
                0,                     // fileCount
                0,                     // commentCount
                false,                 // locked
                false,
                List.of(createFileResponse()),             // files
                List.of()              // comments
        );
    }

    private FileResponse createFileResponse() {
        return new FileResponse(1L, "test.txt", 1024L);
    }
}