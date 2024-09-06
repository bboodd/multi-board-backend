package com.hh.multiboarduserbackend.domain.free.post;

import com.hh.multiboarduserbackend.aop.AuthenticationContextHolder;
import com.hh.multiboarduserbackend.common.dto.SearchDto;
import com.hh.multiboarduserbackend.common.dto.request.FileRequestDto;
import com.hh.multiboarduserbackend.common.dto.response.FileResponseDto;
import com.hh.multiboarduserbackend.common.paging.PaginationDto;
import com.hh.multiboarduserbackend.common.paging.PagingAndListResponse;
import com.hh.multiboarduserbackend.common.response.Response;
import com.hh.multiboarduserbackend.common.utils.FileUtils;
import com.hh.multiboarduserbackend.common.utils.PaginationUtils;
import com.hh.multiboarduserbackend.common.vo.FileVo;
import com.hh.multiboarduserbackend.common.vo.SearchVo;
import com.hh.multiboarduserbackend.domain.free.file.FreeFileService;
import com.hh.multiboarduserbackend.domain.member.LoginMember;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.hh.multiboarduserbackend.common.vo.FileVo.toVoList;
import static com.hh.multiboarduserbackend.common.vo.SearchVo.toVo;
import static com.hh.multiboarduserbackend.domain.free.post.FreePostVo.toVo;

@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api-board/free-board")
public class FreePostController {

    private final FreePostService freePostService;
    private final FreeFileService freeFileService;
    private final FileUtils fileUtils;
    private final PaginationUtils paginationUtils;

    // 게시글 리스트 조회
    @GetMapping("/posts")
    public ResponseEntity<Response> getPosts(SearchDto searchDto) {

        PagingAndListResponse<FreePostResponseDto> data = null;

        int count = freePostService.countAllBySearch(toVo(searchDto));

        if(count >= 1) {
            PaginationDto paginationDto = paginationUtils.createPagination(count ,searchDto);
            List<FreePostResponseDto> listDto = freePostService.findAllBySearch(toVo(searchDto));

            data = new PagingAndListResponse<>(listDto, paginationDto);
        } else {
            data = new PagingAndListResponse<>(Collections.emptyList(), null);
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(Response.data(data));
    }

    // 게시글 조회
    @GetMapping("/posts/{freePostId}")
    public ResponseEntity<Response> getPost(@PathVariable Long freePostId) {

        freePostService.increaseViewCntById(freePostId);

        FreePostResponseDto freePostResponseDto = freePostService.findById(freePostId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(Response.data(freePostResponseDto));
    }

    // 게시글 저장
    @LoginMember
    @PostMapping("/posts")
    public ResponseEntity<Response> savePost(@Valid FreePostRequestDto freePostRequestDto) {

        final Long memberId = AuthenticationContextHolder.getContext();
        final Long freePostId = freePostService.savePost(toVo(freePostRequestDto, memberId));

        // 파일 업로드 및 저장
        List<MultipartFile> fileList = freePostRequestDto.files();
        fileUploadAndSave(fileList, freePostId);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(Response.message(freePostId + "번 게시글 등록완료"));
    }

    // 게시글 수정
    @LoginMember
    @PutMapping("/posts/{freePostId}")
    public ResponseEntity<Response> updatePost(@PathVariable Long freePostId, @Valid FreePostRequestDto freePostRequestDto) {

        final Long memberId = AuthenticationContextHolder.getContext();

        // 수정
        freePostService.updatePost(FreePostVo.toVo(freePostRequestDto, memberId));

        // 파일 업로드 및 저장
        List<MultipartFile> fileList = freePostRequestDto.files();
        fileUploadAndSave(fileList, freePostId);

        // 파일 삭제 from disk and db
        List<Long> removeFileIdList = freePostRequestDto.removeFileIds();
        fileDeleteDiskAndDb(removeFileIdList);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(Response.message(freePostId + "번 게시글 수정 완료"));
    }

    // 게시글 삭제
    @LoginMember
    @DeleteMapping("/posts/{freePostId}")
    public ResponseEntity<Response> deletePost(@PathVariable Long freePostId) {

        final Long memberId = AuthenticationContextHolder.getContext();

        freePostService.deletePostById(freePostId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(Response.message(freePostId + "번 게시글 삭제 완료"));
    }

    // 파일 업로드 및 저장 메서드
    private void fileUploadAndSave(List<MultipartFile> fileList, Long freePostId) {
        if(!CollectionUtils.isEmpty(fileList)) {
            // 업로드
            List<FileRequestDto> uploadFileList = fileUtils.uploadFiles(fileList);

            List<FileVo> fileVoList = toVoList(uploadFileList, freePostId);
            // 저장
            freeFileService.saveFileList(fileVoList);
        }
    }

    // 파일 삭제 메서드
    private void fileDeleteDiskAndDb(List<Long> removeFileIdList) {
        if(!CollectionUtils.isEmpty(removeFileIdList)) {
            // 삭제할 파일 정보 조회
            List<FileResponseDto> deleteFileList = freeFileService.findAllByIds(removeFileIdList);
            // 파일 삭제 from disk
            fileUtils.deleteFiles(deleteFileList);
            // 파일 삭제 from db
            freeFileService.deleteAllByIds(removeFileIdList);
        }
    }

}
