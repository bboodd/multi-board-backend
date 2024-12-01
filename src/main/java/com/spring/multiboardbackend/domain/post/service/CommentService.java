package com.spring.multiboardbackend.domain.post.service;

import com.spring.multiboardbackend.domain.post.exception.CommentErrorCode;
import com.spring.multiboardbackend.domain.post.repository.CommentRepository;
import com.spring.multiboardbackend.domain.post.vo.CommentVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    /**
     * 새로운 댓글을 등록합니다.
     *
     * @param comment 저장할 댓글 정보
     * @return 저장된 댓글 정보
     */
    public CommentVO saveComment(CommentVO comment) {
        commentRepository.save(comment);

        return comment;
    }

    /**
     * 댓글 ID로 댓글을 조회합니다.
     *
     * @param id 조회할 댓글의 ID
     * @return 조회된 댓글 정보
     */
    public CommentVO findById(Long id) {
        return commentRepository.findById(id)
                .orElseThrow(CommentErrorCode.COMMENT_NOT_FOUND::defaultException);
    }

    /**
     * 댓글을 삭제합니다.
     *
     * @param id 삭제할 댓글의 ID
     * @return 삭제 성공 여부
     */
    public boolean deleteById(Long id) {
        commentRepository.deleteById(id);

        return true;
    }
}