package com.hh.multiboarduserbackend.docs;

//import com.hh.board.common.response.ErrorResponse;
//import com.hh.board.common.response.Response;
//import com.hh.board.domain.file.FileResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

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
