package com.hh.multiboarduserbackend.domain.gallery.post;

import com.hh.multiboarduserbackend.aop.AuthenticationContextHolder;
import com.hh.multiboarduserbackend.common.dto.SearchDto;
import com.hh.multiboarduserbackend.common.dto.request.FileRequestDto;
import com.hh.multiboarduserbackend.common.dto.request.PostRequestDto;
import com.hh.multiboarduserbackend.common.dto.response.FileResponseDto;
import com.hh.multiboarduserbackend.common.dto.response.PostResponseDto;
import com.hh.multiboarduserbackend.common.paging.PaginationDto;
import com.hh.multiboarduserbackend.common.paging.PagingAndListResponse;
import com.hh.multiboarduserbackend.common.response.Response;
import com.hh.multiboarduserbackend.common.utils.FileUtils;
import com.hh.multiboarduserbackend.common.utils.PaginationUtils;
import com.hh.multiboarduserbackend.common.vo.FileVo;
import com.hh.multiboarduserbackend.common.vo.PostVo;
import com.hh.multiboarduserbackend.common.vo.SearchVo;
import com.hh.multiboarduserbackend.domain.gallery.file.GalleryFileService;
import com.hh.multiboarduserbackend.domain.gallery.thumbnail.GalleryThumbnailService;
import com.hh.multiboarduserbackend.domain.member.LoginMember;
import com.hh.multiboarduserbackend.exception.FileErrorCode;
import com.hh.multiboarduserbackend.exception.PostErrorCode;
import com.hh.multiboarduserbackend.mappers.FileMapper;
import com.hh.multiboarduserbackend.mappers.PostMapper;
import com.hh.multiboarduserbackend.mappers.SearchMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;

@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api-board/gallery-board")
public class GalleryPostController {

    private final GalleryPostService galleryPostService;
    private final GalleryFileService galleryFileService;
    private final GalleryThumbnailService galleryThumbnailService;

    private final FileUtils fileUtils;
    private final PaginationUtils paginationUtils;

    private final PostMapper postModelMapper = Mappers.getMapper(PostMapper.class);
    private final SearchMapper searchModelMapper = Mappers.getMapper(SearchMapper.class);
    private final FileMapper fileModelMapper = Mappers.getMapper(FileMapper.class);

    // 게시글 리스트 조회
    @GetMapping("/posts")
    public ResponseEntity<?> getPosts(SearchDto searchDto) {

        PagingAndListResponse<PostResponseDto> pagingAndListResponse;

        SearchVo searchVo = searchModelMapper.toVo(searchDto);

        int count = galleryPostService.countAllBySearch(searchVo);

        if(count >= 1) {
            PaginationDto paginationDto = paginationUtils.createPagination(count ,searchDto);

            SearchVo searchVoWithPagination = searchModelMapper.toVoWithPagination(searchDto, paginationDto);
            List<PostVo> postVoList = galleryPostService.findAllBySearch(searchVoWithPagination);

            List<PostResponseDto> postResponseDtoList = postModelMapper.toDtoList(postVoList);

            pagingAndListResponse = new PagingAndListResponse<>(postResponseDtoList, paginationDto);
        } else {
            pagingAndListResponse = new PagingAndListResponse<>(Collections.emptyList(), null);
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(Response.data(pagingAndListResponse));
    }

    // 게시글 조회
    @GetMapping("/posts/{galleryPostId}")
    public ResponseEntity<?> getPost(@PathVariable Long galleryPostId) {

        galleryPostService.increaseViewCntById(galleryPostId);

        PostVo postVo = galleryPostService.findById(galleryPostId).orElseThrow(PostErrorCode.POST_NOT_FOUND::defaultException);

        PostResponseDto postResponseDto = postModelMapper.toDto(postVo);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(Response.data(postResponseDto));
    }

    // 게시글 저장
    @LoginMember
    @PostMapping("/posts")
    public ResponseEntity<?> savePost(@Valid PostRequestDto postRequestDto) {

        final Long memberId = AuthenticationContextHolder.getContext();
        PostVo postVo = postModelMapper.toVoWithMemberId(postRequestDto, memberId);
        final Long galleryPostId = galleryPostService.savePost(postVo);

        // 파일 업로드 및 저장
        List<MultipartFile> fileList = postRequestDto.files();
        fileUploadAndSave(fileList, galleryPostId);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(Response.message(galleryPostId + "번 게시글 등록완료"));
    }

    // 게시글 수정
    @LoginMember
    @PutMapping("/posts/{galleryPostId}")
    public ResponseEntity<?> updatePost(@PathVariable Long galleryPostId, @Valid PostRequestDto postRequestDto) {

        final Long memberId = AuthenticationContextHolder.getContext();

        PostVo postVo = postModelMapper.toVoWithMemberId(postRequestDto, memberId);

        // 수정
        galleryPostService.updatePost(postVo);

        // 파일 삭제 from disk and db
        List<Long> removeFileIdList = postRequestDto.removeFileIds();
        fileDeleteDiskAndDb(removeFileIdList);

        // 파일 업로드 및 저장
        List<MultipartFile> fileList = postRequestDto.files();
        fileUploadAndSave(fileList, galleryPostId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(Response.message(galleryPostId + "번 게시글 수정 완료"));
    }

    // 게시글 삭제
    @LoginMember
    @DeleteMapping("/posts/{galleryPostId}")
    public ResponseEntity<?> deletePost(@PathVariable Long galleryPostId) {

        final Long memberId = AuthenticationContextHolder.getContext();

        galleryPostService.deletePostById(galleryPostId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(Response.message(galleryPostId + "번 게시글 삭제 완료"));
    }

    // 파일 업로드 및 저장 메서드
    private void fileUploadAndSave(List<MultipartFile> fileList, Long galleryPostId) {
        if(!CollectionUtils.isEmpty(fileList)) {
            // 업로드
            List<FileRequestDto> uploadFileList = fileUtils.uploadFiles(fileList);

            // 저장
            List<FileVo> fileVoList = fileModelMapper.toVoListWithPostId(uploadFileList, galleryPostId);

            galleryFileService.saveFileList(fileVoList);

            // 썸네일이 없다면 업로드 및 저장
            if(!galleryThumbnailService.checkExistsThumbnail(galleryPostId)) {
                // db에서 첫 파일 가져와서 변환
                FileVo firstFile = galleryFileService.findFirstByPostId(galleryPostId).orElseThrow(FileErrorCode.FILE_NOT_FOUND::defaultException);
                FileVo uploadThumbnail = fileUtils.uploadThumbnail(firstFile);

                galleryThumbnailService.saveThumbnail(uploadThumbnail);
            }
        }
    }

    // 파일 삭제 메서드
    private void fileDeleteDiskAndDb(List<Long> removeFileIdList) {
        if(!CollectionUtils.isEmpty(removeFileIdList)) {
            // 삭제할 파일 정보 조회
            List<FileVo> fileVoList = galleryFileService.findAllByIds(removeFileIdList);
            List<FileResponseDto> deleteFileList = fileModelMapper.toDtoList(fileVoList);
            // 파일 삭제 from disk
            fileUtils.deleteFiles(deleteFileList);
            // 파일 삭제 from db
            galleryFileService.deleteAllByIds(removeFileIdList);
        }
    }
}
