package com.hh.multiboarduserbackend.domain.ask.answer;

import com.hh.multiboarduserbackend.common.dto.response.CommentResponseDto;
import com.hh.multiboarduserbackend.common.response.Response;
import com.hh.multiboarduserbackend.common.vo.CommentVo;
import com.hh.multiboarduserbackend.domain.free.comment.FreeCommentService;
import com.hh.multiboarduserbackend.mappers.CommentMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api-board/ask-board")
public class AskAnswerController {


    private final AskAnswerService askAnswerService;
    private final CommentMapper commentModelMapper = Mappers.getMapper(CommentMapper.class);

    // 댓글 목록 조회
    @GetMapping("/posts/{askPostId}/answers")
    public ResponseEntity<?> getAnswers(@PathVariable Long askPostId) {

        List<CommentVo> commentVoList = askAnswerService.findAllByPostId(askPostId);
        List<CommentResponseDto> commentDtoList = commentModelMapper.toDtoList((commentVoList));

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(Response.data(commentDtoList));
    }
}
