package com.hh.multiboarduserbackend.domain.ask.answer;


import com.hh.multiboarduserbackend.common.vo.CommentVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class AskAnswerService {

    private final AskAnswerRepository askAnswerRepository;

    /**
     * 답변 리스트 조회
     * @param askPostId - 게시글 아이디
     * @return - 리스트
     */
    public List<CommentVo> findAllByPostId(Long askPostId) {
        return askAnswerRepository.findAllByPostId(askPostId);
    }
}
