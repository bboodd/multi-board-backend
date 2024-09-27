package com.hh.multiboarduserbackend.domain.comment;

import com.hh.multiboarduserbackend.aop.AuthenticationContextHolder;
import com.hh.multiboarduserbackend.common.response.Response;
import com.hh.multiboarduserbackend.domain.comment.request.CommentRequestDto;
import com.hh.multiboarduserbackend.domain.comment.response.CommentResponseDto;
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
@RequestMapping("/api-board")
public class CommentController {

    private final CommentService commentService;
    private final CommentMapper commentModelMapper = Mappers.getMapper(CommentMapper.class);

    // 댓글 목록 조회
    @GetMapping("/{boardType}/posts/{postId}/comments")
    public ResponseEntity<?> getComments(@PathVariable String boardType, @PathVariable Long postId) {

        List<CommentVo> commentVoList = commentService.findAllByPostId(postId);
        List<CommentResponseDto> commentDtoList = commentModelMapper.toDtoList((commentVoList));

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(Response.data(commentDtoList));
    }

    // 댓글 등록
    @LoginMember
    @PostMapping("/{boardType}/posts/{postId}/comments")
    public ResponseEntity<?> saveComment(@PathVariable String boardType, @PathVariable Long postId, @RequestBody @Valid CommentRequestDto commentRequestDto) {

        Long memberId = AuthenticationContextHolder.getContext();

        CommentVo commentVo = commentModelMapper.toVo(commentRequestDto, memberId, postId);

        commentService.saveComment(commentVo);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(Response.message("댓글 등록 완료"));
    }

    //댓글 삭제
    @LoginMember
    @DeleteMapping("/{boardType}/posts/{postId}/comments/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable String boardType, @PathVariable Long postId, @PathVariable Long commentId) {

        commentService.deleteById(commentId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(Response.message("댓글 삭제 완료"));
    }
}
