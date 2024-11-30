package com.spring.multiboardbackend.domain.post.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.spring.multiboardbackend.domain.board.enums.BoardType;
import com.spring.multiboardbackend.domain.post.dto.request.PostRequest;
import com.spring.multiboardbackend.domain.post.dto.response.PostResponse;
import com.spring.multiboardbackend.domain.post.dto.response.PostsResponse;
import com.spring.multiboardbackend.domain.post.service.PostService;
import com.spring.multiboardbackend.global.common.request.SearchRequest;
import com.spring.multiboardbackend.global.common.response.Pagination;
import com.spring.multiboardbackend.global.security.auth.AuthenticationContextHolder;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@DisplayName("게시글 Controller 테스트")
class PostControllerTest {

    @InjectMocks
    private PostController postController;

    @Mock
    private PostService postService;

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
            PostsResponse expectedResponse = createPostsResponse();

            given(postService.findAll(any(SearchRequest.class), eq(BoardType.FREE)))
                    .willReturn(expectedResponse);

            // when & then
            mockMvc.perform(get("/api/boards/{boardType}/posts", boardType)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data").isArray())
                    .andExpect(jsonPath("$.data[0].title").value("테스트 게시글"))
                    .andExpect(jsonPath("$.data[0].content").value("테스트 내용"))
                    .andExpect(jsonPath("$.pagination.currentPage").value(1));
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
            PostResponse expectedResponse = createPostResponse();

            given(postService.findById(postId)).willReturn(expectedResponse);

            // when & then
            mockMvc.perform(get("/api/boards/{boardType}/posts/{postId}", boardType, postId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.title").value("테스트 게시글"))
                    .andExpect(jsonPath("$.content").value("테스트 내용"));

            verify(postService).incrementViewCount(postId);
        }
    }

    @Nested
    @DisplayName("게시글 작성")
    class SavePost {

        @Test
        @DisplayName("게시글 작성 성공")
        void savePost_Success() throws Exception {
            // given
            String boardType = "free";
            Long memberId = 1L;
            PostRequest request = createPostRequest();
            PostResponse expectedResponse = createPostResponse();

            AuthenticationContextHolder.setContext(memberId);
            given(postService.save(any(PostRequest.class), eq(memberId), eq(BoardType.FREE)))
                    .willReturn(expectedResponse);

            MockMultipartFile file = new MockMultipartFile("file", "test.txt", MediaType.TEXT_PLAIN_VALUE, "test file".getBytes());

            // when & then
            mockMvc.perform(multipart("/api/boards/{boardType}/posts", boardType)
                            .file(file)
                            .param("title", request.title())
                            .param("content", request.content())
                            .param("categoryId", String.valueOf(request.categoryId())))
                    .andDo(print())
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.title").value("테스트 게시글"))
                    .andExpect(jsonPath("$.content").value("테스트 내용"));
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
            PostResponse expectedResponse = createPostResponse();

            AuthenticationContextHolder.setContext(memberId);
            given(postService.update(any(PostRequest.class), eq(memberId), eq(postId), eq(BoardType.FREE)))
                    .willReturn(expectedResponse);

            // when & then
            mockMvc.perform(put("/api/boards/{boardType}/posts/{postId}", boardType, postId)
                            .contentType(MediaType.MULTIPART_FORM_DATA)
                            .param("title", request.title())
                            .param("content", request.content())
                            .param("categoryId", String.valueOf(request.categoryId())))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.title").value("테스트 게시글"))
                    .andExpect(jsonPath("$.content").value("테스트 내용"));
        }
    }

    @Nested
    @DisplayName("게시글 삭제")
    class DeletePost {

        @Test
        @DisplayName("게시글 삭제 성공")
        void deletePost_Success() throws Exception {
            // given
            String boardType = "FREE";
            Long postId = 1L;
            Long memberId = 1L;

            AuthenticationContextHolder.setContext(memberId);
            given(postService.delete(postId, memberId)).willReturn(true);

            // when & then
            mockMvc.perform(delete("/api/boards/{boardType}/posts/{postId}", boardType, postId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").value(true));
        }
    }

    private PostRequest createPostRequest() {
        return PostRequest.builder()
                .categoryId(1L)
                .title("테스트 게시글")
                .content("테스트 내용")
                .build();
    }

    private PostResponse createPostResponse() {
        return new PostResponse(
                1L,                // categoryId
                "테스트 게시글",          // title
                "테스트 내용",           // content
                0,                     // viewCount
                LocalDateTime.now(),   // createdAt
                "자유",                 // categoryName
                "테스트유저",             // nickname
                0,                     // fileCount
                0,                     // commentCount
                null,                  // thumbnailUrl
                false,                 // locked
                List.of(),             // files
                List.of()              // comments
        );
    }

    private PostsResponse createPostsResponse() {
        return new PostsResponse(
                List.of(createPostResponse()),
                new Pagination(1, 1, false)
        );
    }
}