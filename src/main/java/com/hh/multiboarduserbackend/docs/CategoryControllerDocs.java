package com.hh.multiboarduserbackend.docs;

//import com.hh.board.common.response.Response;
//import com.hh.board.domain.category.CategoryDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "categories", description = "카테고리 API")
public interface CategoryControllerDocs {

//    @Operation(summary = "list", description = "카테고리 리스트 조회")
//    @ApiResponses({
//            @ApiResponse(responseCode = "200", description = "카테고리 리스트 조회 성공", content = @Content(schema = @Schema(implementation = CategoryDto.class)))
//    })
//    public ResponseEntity<Response> getCategories();
}
