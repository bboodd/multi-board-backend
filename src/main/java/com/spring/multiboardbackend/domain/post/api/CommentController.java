package com.spring.multiboardbackend.domain.post.api;

import com.spring.multiboardbackend.domain.member.service.AuthService;
import com.spring.multiboardbackend.domain.post.docs.CommentControllerDocs;
import com.spring.multiboardbackend.domain.post.exception.CommentErrorCode;
import com.spring.multiboardbackend.domain.post.mapper.CommentMapper;
import com.spring.multiboardbackend.domain.post.service.CommentService;
import com.spring.multiboardbackend.domain.post.vo.CommentVO;
import com.spring.multiboardbackend.global.security.auth.AuthenticationContextHolder;
import com.spring.multiboardbackend.domain.post.dto.request.CommentRequest;
import com.spring.multiboardbackend.domain.post.dto.response.CommentResponse;
import com.spring.multiboardbackend.domain.member.annotation.LoginMember;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api/boards")
public class CommentController implements CommentControllerDocs {

    private final CommentService commentService;
    private final CommentMapper commentMapper;
    private final AuthService authService;

    /**
     * 댓글 등록
     */
    @LoginMember
    @PostMapping("/{boardType}/posts/{postId}/comments")
    public ResponseEntity<CommentResponse> saveComment(@PathVariable String boardType, @PathVariable Long postId, @RequestBody @Valid CommentRequest request) {

        Long memberId = AuthenticationContextHolder.getContext();

        CommentVO comment = commentService.saveComment(commentMapper.toVO(request, memberId, postId));

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(commentMapper.toResponse(comment));
    }

    /**
     * 댓글 삭제
     */
    @LoginMember
    @DeleteMapping("/{boardType}/posts/{postId}/comments/{commentId}")
    public ResponseEntity<Boolean> deleteComment(@PathVariable String boardType, @PathVariable Long postId, @PathVariable Long commentId) {

        Long memberId = AuthenticationContextHolder.getContext();

        CommentVO comment = commentService.findById(commentId);

        if (!comment.getMemberId().equals(memberId) && !authService.isAdmin(memberId)) {
            throw CommentErrorCode.PERMISSION_DENIED.defaultException();
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(commentService.deleteById(commentId));
    }

    /**
     * 댓글 조회
     */
    @GetMapping("/{boardType}/posts/{postId}/comments")
    public ResponseEntity<List<CommentResponse>> getComments(@PathVariable String boardType, @PathVariable Long postId) {

        List<CommentVO> comments = commentService.findAllByPostId(postId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(commentMapper.toResponseList(comments));
    }
}
