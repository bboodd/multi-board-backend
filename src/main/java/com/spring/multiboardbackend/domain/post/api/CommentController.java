package com.spring.multiboardbackend.domain.post.api;

import com.spring.multiboardbackend.domain.post.docs.CommentControllerDocs;
import com.spring.multiboardbackend.domain.post.service.CommentService;
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

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api/boards")
public class CommentController implements CommentControllerDocs {

    private final CommentService commentService;

    // 댓글 등록
    @LoginMember
    @PostMapping("/{boardType}/posts/{postId}/comments")
    public ResponseEntity<CommentResponse> saveComment(@PathVariable String boardType, @PathVariable Long postId, @RequestBody @Valid CommentRequest commentRequest) {

        Long memberId = AuthenticationContextHolder.getContext();

        CommentResponse commentResponse = commentService.saveComment(commentRequest, memberId, postId);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(commentResponse);
    }

    //댓글 삭제
    @LoginMember
    @DeleteMapping("/{boardType}/posts/{postId}/comments/{commentId}")
    public ResponseEntity<Boolean> deleteComment(@PathVariable String boardType, @PathVariable Long postId, @PathVariable Long commentId) {

        Long memberId = AuthenticationContextHolder.getContext();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(commentService.deleteById(commentId, memberId));
    }
}
