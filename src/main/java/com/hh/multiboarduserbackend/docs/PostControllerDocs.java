package com.hh.multiboarduserbackend.docs;

import com.hh.board.common.dto.PasswordRequestDto;
import com.hh.board.common.dto.SearchDto;
import com.hh.board.common.paging.PagingResponse;
import com.hh.board.common.response.ErrorResponse;
import com.hh.board.common.response.Response;
import com.hh.board.domain.post.PostRequestDto;
import com.hh.board.domain.post.PostResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "posts", description = "게시글 API")
public interface PostControllerDocs {

    @Operation(summary = "list", description = "게시글 리스트 조회 || 파라미터 없을 경우 기본값 적용")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "게시글 리스트 조회 성공", content = @Content(schema = @Schema(implementation = PagingResponse.class)))
    })
    @Parameters({
            @Parameter(name = "startDate", description = "검색 시작일"),
            @Parameter(name = "endDate", description = "검색 종료일"),
            @Parameter(name = "categoryId", description = "카테고리 아이디값"),
            @Parameter(name = "keyword", description = "검색어"),
            @Parameter(name = "page", description = "현재 페이지"),
            @Parameter(name = "recordSize", description = "페이지당 출력할 데이터 개수"),
            @Parameter(name = "pageSize", description = "화면 하단 출력할 페이지 사이즈")
    })
    public ResponseEntity<Response> getPosts(SearchDto searchDto);

    @Operation(summary = "detail", description = "게시글 단건 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "게시글 단건 조회 성공", content = @Content(schema = @Schema(implementation = PostResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 게시글 ID 입력", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @Parameters({
            @Parameter(name = "postId", description = "게시글 아이디")
    })
    public ResponseEntity<Response> getPost(int postId);

    @Operation(summary = "write", description = "게시글 등록")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "게시글 등록 성공"),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 내용 입력", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @Parameters({
            @Parameter(name = "categoryId", description = "카테고리 아이디"),
            @Parameter(name = "writer", description = "작성자 이름"),
            @Parameter(name = "password", description = "비밀번호"),
            @Parameter(name = "checkPassword", description = "비밀번호 확인"),
            @Parameter(name = "title", description = "게시글 제목"),
            @Parameter(name = "content", description = "게시글 내용"),
            @Parameter(name = "files", description = "파일")
    })
    public ResponseEntity<Response> savePost(PostRequestDto postRequestDto);

    @Operation(summary = "update", description = "게시글 수정")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "게시글 수정 성공"),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 내용 입력", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @Parameters({
            @Parameter(name = "categoryId", description = "카테고리 아이디"),
            @Parameter(name = "writer", description = "작성자 이름"),
            @Parameter(name = "password", description = "비밀번호"),
            @Parameter(name = "checkPassword", description = "비밀번호 확인"),
            @Parameter(name = "title", description = "게시글 제목"),
            @Parameter(name = "content", description = "게시글 내용"),
            @Parameter(name = "files", description = "파일"),
            @Parameter(name = "removeFileIds", description = "삭제할 파일 아이디 리스트")
    })
    public ResponseEntity<Response> updatePost(int postId, PostRequestDto postRequestDto);

    @Operation(summary = "delete", description = "게시글 삭제")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "게시글 삭제 성공")
    })
    @Parameters({
            @Parameter(name = "postId", description = "게시글 아이디")
    })
    public ResponseEntity<Response> deletePost(int postId);

    @Operation(summary = "check", description = "비밀번호 확인")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "비밀번호 일치"),
            @ApiResponse(responseCode = "400", description = "비밀번호 불일치", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @Parameters({
            @Parameter(name = "postId", description = "게시글 아이디"),
            @Parameter(name = "inputPassword", description = "비밀번호 입력")
    })
    ResponseEntity<Response> checkPassword(@PathVariable int postId, @RequestBody PasswordRequestDto passwordRequestDto);
}
