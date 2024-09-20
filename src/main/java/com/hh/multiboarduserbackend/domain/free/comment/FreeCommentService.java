package com.hh.multiboarduserbackend.domain.free.comment;

import com.hh.multiboarduserbackend.common.dto.response.CommentResponseDto;
import com.hh.multiboarduserbackend.common.vo.CommentVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
@Slf4j
@RequiredArgsConstructor
public class FreeCommentService {

    private final FreeCommentRepository freeCommentRepository;

    /**
     * 댓글 등록
     * @param commentVo - 댓글 정보
     */
    public void saveComment(CommentVo commentVo) {
        freeCommentRepository.save(commentVo);
    }

    /**
     * 댓글 리스트 조회
     * @param freePostId - 게시글 아이디
     * @return - 리스트
     */
    public List<CommentVo> findAllByPostId(Long freePostId) {
        return freeCommentRepository.findAllByPostId(freePostId);
    }

    /**
     * 댓글 삭제
     * @param freeCommentId - 댓글 아이디
     */
    public void deleteById(Long freeCommentId) {
        freeCommentRepository.deleteById(freeCommentId);
    }
}
