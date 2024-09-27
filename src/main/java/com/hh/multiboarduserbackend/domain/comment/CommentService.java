package com.hh.multiboarduserbackend.domain.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    /**
     * 댓글 등록
     * @param commentVo - 댓글 정보
     */
    public void saveComment(CommentVo commentVo) {
        commentRepository.save(commentVo);
    }

    /**
     * 댓글 리스트 조회
     * @param postId - 게시글 아이디
     * @return - 리스트
     */
    public List<CommentVo> findAllByPostId(Long postId) {
        return commentRepository.findAllByPostId(postId);
    }

    /**
     * 댓글 삭제
     * @param commentId - 댓글 pk
     */
    public void deleteById(Long commentId) {
        commentRepository.deleteById(commentId);
    }
}
