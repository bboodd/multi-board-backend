package com.spring.multiboardbackend.domain.post.api;

import com.spring.multiboardbackend.domain.member.service.AuthService;
import com.spring.multiboardbackend.domain.post.docs.PostControllerDocs;
import com.spring.multiboardbackend.domain.post.dto.response.FileResponse;
import com.spring.multiboardbackend.domain.post.dto.response.PostsResponse;
import com.spring.multiboardbackend.domain.post.exception.PostErrorCode;
import com.spring.multiboardbackend.domain.post.mapper.FileMapper;
import com.spring.multiboardbackend.domain.post.mapper.PostMapper;
import com.spring.multiboardbackend.domain.post.service.FileService;
import com.spring.multiboardbackend.domain.post.service.PostService;
import com.spring.multiboardbackend.domain.post.vo.FileVO;
import com.spring.multiboardbackend.domain.post.vo.PostVO;
import com.spring.multiboardbackend.global.common.mapper.SearchMapper;
import com.spring.multiboardbackend.global.common.response.Pagination;
import com.spring.multiboardbackend.global.common.vo.SearchVO;
import com.spring.multiboardbackend.global.common.request.SearchRequest;
import com.spring.multiboardbackend.domain.board.enums.BoardType;
import com.spring.multiboardbackend.domain.post.dto.request.PostRequest;
import com.spring.multiboardbackend.domain.post.dto.response.PostResponse;
import com.spring.multiboardbackend.global.security.util.SecurityUtil;
import com.spring.multiboardbackend.global.util.FileUtils;
import com.spring.multiboardbackend.global.util.UploadedFile;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api/boards")
public class PostController implements PostControllerDocs {

    private final PostService postService;
    private final PostMapper postMapper;
    private final SearchMapper searchMapper;
    private final FileMapper fileMapper;
    private final FileService fileService;
    private final FileUtils fileUtils;
    private final AuthService authService;
    private final SecurityUtil securityUtil;

    private static final List<BoardType> FILE_SUPPORT_BOARDS = List.of(BoardType.FREE, BoardType.GALLERY);

    /**
     * 대시보드 데이터 조회
     */
    @GetMapping("/dashboard")
    @Operation(summary = "게시판마다 게시글 일부 조회", description = "게시판들의 게시글 목록을 조회합니다.")
    public ResponseEntity<Map<String, List<PostResponse>>> getDashboardPosts() {

        Map<Long, List<PostVO>> dashboardPosts = postService.findDashboardPosts();

        Map<String, List<PostResponse>> response = new HashMap<>();
        response.put("freePosts", postMapper.toResponseList(dashboardPosts.get(BoardType.FREE.getId())));
        response.put("galleryPosts", postMapper.toResponseList(dashboardPosts.get(BoardType.GALLERY.getId())));
        response.put("qnaPosts", postMapper.toResponseList(dashboardPosts.get(BoardType.QNA.getId())));
        response.put("noticePosts", postMapper.toResponseList(dashboardPosts.get(BoardType.NOTICE.getId())));

        return ResponseEntity.ok(response);
    }

    /**
     * 게시글 목록 조회
     */
    @GetMapping("/{boardType}/posts")
    public ResponseEntity<PostsResponse> getPosts(@PathVariable String boardType, SearchRequest request) {

        BoardType type = BoardType.from(boardType);

        SearchVO search = searchMapper.toVO(request, type.getId());

        int totalCount = postService.countBySearch(search);
        if (totalCount == 0) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(PostsResponse.empty());
        }

        List<PostResponse> posts = postMapper.toResponseList(postService.findAll(search));
        Pagination pagination = Pagination.of(request.page(), totalCount, request.size());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(PostsResponse.of(posts, pagination));
    }

    /**
     * 게시글 상세 조회
     */

    @GetMapping("/{boardType}/posts/{postId}")
    public ResponseEntity<PostResponse> getPost(@PathVariable String boardType, @PathVariable Long postId) {

        BoardType.from(boardType);

        postService.incrementViewCount(postId);

        PostResponse post = postMapper.toResponse(postService.findByIdWithDetail(postId));

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(post);
    }

    /**
     * 게시글 작성
     */
    @PostMapping(value = "/{boardType}/posts",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PostResponse> savePost(@PathVariable String boardType, @Valid PostRequest request) {

        Long memberId = securityUtil.getCurrentMemberId();
        BoardType type = BoardType.from(boardType);

        if (type != BoardType.QNA && request.categoryId() == null) {
            throw PostErrorCode.REQUIRED_CATEGORY.defaultException();
        }

        // 게시글 저장
        PostVO post = postService.save(postMapper.toVO(request, memberId, type.getId()));

        if (isFileSupportBoard(type)) {
            List<FileResponse> savedFiles = processFiles(request.files(), post.getId(), type);
            PostResponse response = postMapper.toResponseWithFile(post, savedFiles);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response);
        }

        PostResponse response = postMapper.toResponse(post);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }


    /**
     * 게시글 수정
     */
    @PutMapping(value = "/{boardType}/posts/{postId}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PostResponse> updatePost(@PathVariable String boardType, @PathVariable Long postId, @Valid PostRequest request) {

        Long memberId = securityUtil.getCurrentMemberId();
        BoardType type = BoardType.from(boardType);

        PostVO oldPost = postService.findById(postId);

        if (!oldPost.getMemberId().equals(memberId) && !authService.isAdmin(memberId)) {
            throw PostErrorCode.PERMISSION_DENIED.defaultException();
        }

        // 게시글 수정
        PostVO updatedPost = postService.update(postMapper.toVOForUpdate(request, postId));


        if (isFileSupportBoard(type)) {
            // 파일 삭제
            if (!CollectionUtils.isEmpty(request.removeFileIds())) {
                List<FileVO> files = fileService.findAllByIds(request.removeFileIds());
                fileUtils.deleteFiles(fileMapper.toUploadedFileList(files));
                fileService.deleteAllByIds(request.removeFileIds());
            }

            // 새로운 파일 업로드
            List<FileResponse> savedFiles = processFiles(request.files(), postId, type);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(postMapper.toResponseWithFile(updatedPost, savedFiles));
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(postMapper.toResponse(updatedPost));
    }

    /**
     * 게시글 삭제
     */
    @DeleteMapping("/{boardType}/posts/{postId}")
    public ResponseEntity<Boolean> deletePost(@PathVariable String boardType, @PathVariable Long postId) {

        Long memberId = securityUtil.getCurrentMemberId();

        PostVO post = postService.findById(postId);

        if (!post.getMemberId().equals(memberId) && !authService.isAdmin(memberId)) {
            throw PostErrorCode.PERMISSION_DENIED.defaultException();
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(postService.delete(postId));
    }

    /**
     * 게시판 타입의 파일 지원 여부 확인
     *
     * @param type 게시판 타입
     * @return 파일 지원 여부
     */
    private boolean isFileSupportBoard(BoardType type) {
        return FILE_SUPPORT_BOARDS.contains(type);
    }

    /**
     * 파일 처리 함수
     * @param files multipartFile
     * @param postId 게시글 ID
     * @param type 게시판 타입
     * @return fileResponse
     */
    private List<FileResponse> processFiles(List<MultipartFile> files, Long postId, BoardType type) {
        if (CollectionUtils.isEmpty(files) || files.get(0).isEmpty()) {
            return Collections.emptyList();
        }

        List<UploadedFile> uploadedFiles = fileUtils.uploadFiles(files);

        List<FileVO> fileVOList = fileMapper.toVOList(uploadedFiles, postId);

        if (!fileVOList.isEmpty()) {
            fileService.saveFiles(fileVOList);
        }

        if (type.equals(BoardType.GALLERY)) {
            processThumbnail(postId);
        }

        return fileMapper.toResponseList(fileVOList);
    }

    /**
     * 썸네일 처리
     * @param postId 게시글 ID
     */
    private void processThumbnail(Long postId) {
        if (!postService.hasThumbnail(postId)) {
            FileVO fileVo = fileService.findFirstByPostId(postId);

            UploadedFile thumbnail = fileUtils.uploadThumbnail(fileMapper.toUploadedFile(fileVo));

            fileService.saveThumbnail(fileMapper.toThumbnailVO(thumbnail, postId, fileVo.getId()));
        }
    }

}
