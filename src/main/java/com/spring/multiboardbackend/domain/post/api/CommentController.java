package com.spring.multiboardbackend.domain.post.api;

import com.spring.multiboardbackend.domain.member.service.AuthService;
import com.spring.multiboardbackend.domain.post.docs.CommentControllerDocs;
import com.spring.multiboardbackend.domain.post.exception.CommentErrorCode;
import com.spring.multiboardbackend.domain.post.mapper.CommentMapper;
import com.spring.multiboardbackend.domain.post.service.CommentService;
import com.spring.multiboardbackend.domain.post.vo.CommentVO;
import com.spring.multiboardbackend.domain.post.dto.request.CommentRequest;
import com.spring.multiboardbackend.domain.post.dto.response.CommentResponse;
import com.spring.multiboardbackend.global.security.util.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
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
@RequestMapping("/api/posts")
public class CommentController implements CommentControllerDocs {

    private final CommentService commentService;
    private final CommentMapper commentMapper;
    private final AuthService authService;
    private final SecurityUtil securityUtil;

    /**
     * 댓글 등록
     */
    @PostMapping("/{postId}/comments")
    public ResponseEntity<CommentResponse> saveComment(@PathVariable Long postId, @RequestBody @Valid CommentRequest request) {

        Long memberId = securityUtil.getCurrentMemberId();

        CommentVO comment = commentService.saveComment(commentMapper.toVO(request, memberId, postId));

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(commentMapper.toResponse(comment));
    }

    /**
     * 댓글 삭제
     */
    @DeleteMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<Boolean> deleteComment(@PathVariable Long postId, @PathVariable Long commentId) {

        Long memberId = securityUtil.getCurrentMemberId();

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
    @GetMapping("/{postId}/comments")
    @Operation(summary = "댓글 목록 조회", description = "댓글 목록을 조회합니다.")
    public ResponseEntity<List<CommentResponse>> getComments(@PathVariable Long postId) {

        List<CommentVO> comments = commentService.findAllByPostId(postId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(commentMapper.toResponseList(comments));
    }
}
