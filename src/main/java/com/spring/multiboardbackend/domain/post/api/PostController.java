package com.spring.multiboardbackend.domain.post.api;

import com.spring.multiboardbackend.domain.post.docs.PostControllerDocs;
import com.spring.multiboardbackend.domain.post.dto.response.PostsResponse;
import com.spring.multiboardbackend.domain.post.service.PostService;
import com.spring.multiboardbackend.global.security.auth.AuthenticationContextHolder;
import com.spring.multiboardbackend.global.common.request.SearchRequest;
import com.spring.multiboardbackend.domain.board.enums.BoardType;
import com.spring.multiboardbackend.domain.member.annotation.LoginMember;
import com.spring.multiboardbackend.domain.post.dto.request.PostRequest;
import com.spring.multiboardbackend.domain.post.dto.response.PostResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api/boards")
public class PostController implements PostControllerDocs {

    private final PostService postService;

    /**
     * 게시글 목록 조회
     */
    @GetMapping("/{boardType}/posts")
    public ResponseEntity<PostsResponse> getPosts(@PathVariable String boardType, SearchRequest request) {

        BoardType type = BoardType.from(boardType);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(postService.findAll(request, type));
    }

    /**
     * 게시글 상세 조회
     */

    @GetMapping("/{boardType}/posts/{postId}")
    public ResponseEntity<PostResponse> getPost(@PathVariable String boardType, @PathVariable Long postId) {

        BoardType.from(boardType);

        postService.incrementViewCount(postId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(postService.findById(postId));
    }

    /**
     * 게시글 작성
     */
    @LoginMember
    @PostMapping(value = "/{boardType}/posts", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PostResponse> savePost(@PathVariable String boardType, @Valid PostRequest request) {

        Long memberId = AuthenticationContextHolder.getContext();
        BoardType type = BoardType.from(boardType);

        // 게시글 저장
        PostResponse post = postService.save(request, memberId, type);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(post);
    }


    // 게시글 수정
    @LoginMember
    @PutMapping(value = "/{boardType}/posts/{postId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PostResponse> updatePost(@PathVariable String boardType, @PathVariable Long postId, @Valid PostRequest request) {

        Long memberId = AuthenticationContextHolder.getContext();
        BoardType type = BoardType.from(boardType);

        // 게시글 수정
        PostResponse post = postService.update(request, memberId, postId, type);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(post);
    }

    // 게시글 삭제
    @LoginMember
    @DeleteMapping("/{boardType}/posts/{postId}")
    public ResponseEntity<Boolean> deletePost(@PathVariable String boardType, @PathVariable Long postId) {

        Long memberId = AuthenticationContextHolder.getContext();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(postService.delete(postId, memberId));
    }


}
