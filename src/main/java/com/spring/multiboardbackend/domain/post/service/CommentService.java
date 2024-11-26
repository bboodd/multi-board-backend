package com.spring.multiboardbackend.domain.post.service;

import com.spring.multiboardbackend.domain.member.service.AuthService;
import com.spring.multiboardbackend.domain.post.dto.request.CommentRequest;
import com.spring.multiboardbackend.domain.post.dto.response.CommentResponse;
import com.spring.multiboardbackend.domain.post.exception.CommentErrorCode;
import com.spring.multiboardbackend.domain.post.mapper.CommentMapper;
import com.spring.multiboardbackend.domain.post.repository.CommentRepository;
import com.spring.multiboardbackend.domain.post.vo.CommentVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final AuthService authService;

    /**
     * 게시글에 댓글 등록
     *
     * @param commentRequest 댓글 등록 정보
     * @param memberId 작성자 ID
     * @param postId 게시글 ID
     * @return 등록된 댓글 Response
     */
    public CommentResponse saveComment(CommentRequest commentRequest, Long memberId, Long postId) {
        CommentVO commentVo = commentMapper.toVO(commentRequest, memberId, postId);
        commentRepository.save(commentVo);

        return commentMapper.toResponse(commentVo);
    }

    /**
     * 댓글 삭제 처리
     *
     * @param commentId 삭제할 댓글 ID
     * @param memberId 요청자 ID
     * @return 삭제 성공 여부
     */
    public boolean deleteById(Long commentId, Long memberId) {
        CommentVO comment = commentRepository.findById(commentId)
                .orElseThrow(CommentErrorCode.COMMENT_NOT_FOUND::defaultException);

        if (!comment.getMemberId().equals(memberId) && !authService.isAdmin(memberId)) {
            throw CommentErrorCode.PERMISSION_DENIED.defaultException();
        }

        commentRepository.deleteById(commentId);

        return true;
    }
}