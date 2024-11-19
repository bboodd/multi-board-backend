package com.spring.multiboardbackend.domain.post.docs;

//import com.hh.board.common.response.ErrorResponse;
//import com.hh.board.common.response.Response;
//import com.hh.board.domain.file.FileResponseDto;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "files", description = "파일 API")
public interface FileControllerDocs {

//    @Operation(summary = "list", description = "파일 리스트 조회")
//    @ApiResponses({
//            @ApiResponse(responseCode = "200", description = "파일 리스트 조회 성공", content = @Content(schema = @Schema(implementation = FileResponseDto.class)))
//    })
//    @Parameters({
//            @Parameter(name = "postId", description = "게시글 아이디")
//    })
//    public ResponseEntity<Response> getFiles(int postId);
//
//    @Operation(summary = "download", description = "파일 다운로드")
//    @ApiResponses({
//            @ApiResponse(responseCode = "200", description = "파일 다운로드 성공"),
//            @ApiResponse(responseCode = "404", description = "파일을 찾을 수 없습니다", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
//    })
//    @Parameters({
//            @Parameter(name = "postId", description = "게시글 아이디"),
//            @Parameter(name = "fileId", description = "파일 아이디")
//    })
//    public ResponseEntity<Resource> downloadFile(int postId, int fileId);
}
