package com.hh.multiboarduserbackend.docs;


import com.hh.board.common.response.ErrorResponse;
import com.hh.board.common.response.Response;
import com.hh.board.domain.comment.CommentRequestDto;
import com.hh.board.domain.comment.CommentResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "comments", description = "댓글 API")
public interface CommentControllerDocs {

    @Operation(summary = "list", description = "댓글 리스트 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "댓글 리스트 조회 성공", content = @Content(schema = @Schema(implementation = CommentResponseDto.class)))
    })
    @Parameters({
            @Parameter(name = "postId", description = "게시글 아이디")
    })
    public ResponseEntity<Response> getComments(int postId);

    @Operation(summary = "detail", description = "댓글 단건 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "댓글 단건 조회 성공", content = @Content(schema = @Schema(implementation = CommentResponseDto.class)))
    })
    @Parameters({
            @Parameter(name = "postId", description = "게시글 아이디"),
            @Parameter(name = "commentId", description = "댓글 아이디")
    })
    public ResponseEntity<Response> getComment(int postId, int commentId);

    @Operation(summary = "write", description = "댓글 등록")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "댓글 등록 성공"),
            @ApiResponse(responseCode = "400", description = "내용을 입력해 주세요", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @Parameters({
            @Parameter(name = "postId", description = "게시글 아이디")
    })
    public ResponseEntity<Response> saveComment(int postId, CommentRequestDto commentRequestDto);

    @Operation(summary = "update", description = "댓글 수정")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "댓글 수정 성공"),
            @ApiResponse(responseCode = "400", description = "내용을 입력해 주세요", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @Parameters({
            @Parameter(name = "postId", description = "게시글 아이디"),
            @Parameter(name = "commentId", description = "댓글 아이디")
    })
    public ResponseEntity<Response> updateComment(int postId, int commentId, CommentRequestDto commentRequestDto);

    @Operation(summary = "delete", description = "댓글 삭제")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "댓글 삭제 성공")
    })
    @Parameters({
            @Parameter(name = "postId", description = "게시글 아이디"),
            @Parameter(name = "commentId", description = "댓글 아이디")
    })
    public ResponseEntity<Response> deleteComment(int postId, int commentId);
}
