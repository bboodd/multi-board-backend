package com.hh.multiboarduserbackend.domain.free.comment;

import com.hh.multiboarduserbackend.aop.AuthenticationContextHolder;
import com.hh.multiboarduserbackend.common.dto.request.CommentRequestDto;
import com.hh.multiboarduserbackend.common.dto.response.CommentResponseDto;
import com.hh.multiboarduserbackend.common.response.Response;
import com.hh.multiboarduserbackend.common.vo.CommentVo;
import com.hh.multiboarduserbackend.domain.member.LoginMember;
import com.hh.multiboarduserbackend.mappers.CommentMapper;
import jakarta.validation.Valid;
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
@RequestMapping("/api-board/free-board")
public class FreeCommentController {

    private final FreeCommentService freeCommentService;
    private final CommentMapper commentModelMapper = Mappers.getMapper(CommentMapper.class);

    // 댓글 목록 조회
    @GetMapping("/posts/{freePostId}/comments")
    public ResponseEntity<Response> getComments(@PathVariable Long freePostId) {

        List<CommentVo> commentVoList = freeCommentService.findAllByPostId(freePostId);
        List<CommentResponseDto> commentDtoList = commentModelMapper.toDtoList((commentVoList));

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(Response.data(commentDtoList));
    }

    // 댓글 등록
    @LoginMember
    @PostMapping("/posts/{freePostId}/comments")
    public ResponseEntity<Response> saveComment(@PathVariable Long freePostId, @RequestBody @Valid CommentRequestDto commentRequestDto) {

        Long memberId = AuthenticationContextHolder.getContext();

        CommentVo commentVo = commentModelMapper.toVo(commentRequestDto, memberId, freePostId);

        freeCommentService.saveComment(commentVo);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(Response.message("댓글 등록 완료"));
    }

    //댓글 삭제
    @LoginMember
    @DeleteMapping("/posts/{freePostId}/comments/{freeCommentId}")
    public ResponseEntity<Response> deleteComment(@PathVariable Long freePostId, @PathVariable Long freeCommentId) {

        freeCommentService.deleteById(freeCommentId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(Response.message("댓글 삭제 완료"));
    }
}
