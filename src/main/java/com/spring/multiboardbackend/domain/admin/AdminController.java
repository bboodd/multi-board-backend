package com.spring.multiboardbackend.domain.admin;

import com.spring.multiboardbackend.domain.board.dto.response.CategoryResponse;
import com.spring.multiboardbackend.domain.board.enums.BoardType;
import com.spring.multiboardbackend.domain.board.mapper.CategoryMapper;
import com.spring.multiboardbackend.domain.board.service.CategoryService;
import com.spring.multiboardbackend.domain.board.vo.CategoryVO;
import com.spring.multiboardbackend.domain.member.dto.request.LoginRequest;
import com.spring.multiboardbackend.domain.member.service.AuthService;
import com.spring.multiboardbackend.domain.member.vo.MemberVO;
import com.spring.multiboardbackend.domain.post.dto.request.CommentRequest;
import com.spring.multiboardbackend.domain.post.dto.request.PostRequest;
import com.spring.multiboardbackend.domain.post.dto.response.CommentResponse;
import com.spring.multiboardbackend.domain.post.dto.response.FileResponse;
import com.spring.multiboardbackend.domain.post.dto.response.PostResponse;
import com.spring.multiboardbackend.domain.post.dto.response.PostsResponse;
import com.spring.multiboardbackend.domain.post.exception.CommentErrorCode;
import com.spring.multiboardbackend.domain.post.exception.FileErrorCode;
import com.spring.multiboardbackend.domain.post.exception.PostErrorCode;
import com.spring.multiboardbackend.domain.post.mapper.CommentMapper;
import com.spring.multiboardbackend.domain.post.mapper.FileMapper;
import com.spring.multiboardbackend.domain.post.mapper.PostMapper;
import com.spring.multiboardbackend.domain.post.service.CommentService;
import com.spring.multiboardbackend.domain.post.service.FileService;
import com.spring.multiboardbackend.domain.post.service.PostService;
import com.spring.multiboardbackend.domain.post.vo.CommentVO;
import com.spring.multiboardbackend.domain.post.vo.FileVO;
import com.spring.multiboardbackend.domain.post.vo.PostVO;
import com.spring.multiboardbackend.global.common.mapper.SearchMapper;
import com.spring.multiboardbackend.global.common.request.SearchRequest;
import com.spring.multiboardbackend.global.common.response.Pagination;
import com.spring.multiboardbackend.global.common.vo.SearchVO;
import com.spring.multiboardbackend.global.exception.CustomException;
import com.spring.multiboardbackend.global.exception.ErrorCode;
import com.spring.multiboardbackend.global.security.auth.AuthenticationContextHolder;
import com.spring.multiboardbackend.global.util.FileUtils;
import com.spring.multiboardbackend.global.util.UploadedFile;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
@Slf4j
public class AdminController {

    private final AuthService authService;
    private final PostService postService;
    private final CategoryService categoryService;
    private final FileService fileService;
    private final CommentService commentService;
    private final SearchMapper searchMapper;
    private final PostMapper postMapper;
    private final CategoryMapper categoryMapper;
    private final FileMapper fileMapper;
    private final CommentMapper commentMapper;
    private final FileUtils fileUtils;

    private static final List<BoardType> FILE_SUPPORT_BOARDS = List.of(BoardType.FREE, BoardType.GALLERY);

    // 로그인 페이지
    @GetMapping("/login")
    public String loginPage() {
        return "admin/login";
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/admin/login";
    }

    @PostMapping("/login")
    public String login(
            @Valid LoginRequest request,
            BindingResult bindingResult,
            HttpSession session,
            RedirectAttributes ra
    ) {

        if (bindingResult.hasErrors()) {
            ra.addFlashAttribute("errorMessage", "아이디와 비밀번호를 입력해주세요.");
            return "redirect:/admin/login";
        }

        try{
            MemberVO member = authService.login(request.loginId(), request.password());

            if (authService.isAdmin(member.getId())) {
                // 세션에 관리자 정보 저장
                session.setAttribute("ADMIN_ID", member.getId());
                session.setAttribute("ADMIN_NAME", member.getNickname());

                // 리다이렉트 URL이 있으면 해당 URL로, 없으면 대시보드로
                return "redirect:/admin/dashboard";
            } else {
                // 관리자가 아닌 경우
                ra.addFlashAttribute("errorMessage", "관리자 권한이 없습니다.");
                return "redirect:/admin/login";
            }
        } catch (CustomException e) {
            ra.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/admin/login";
        }

    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        // 대시보드에 표시할 데이터 조회
        Map<Long, List<PostVO>> dashboardPosts = postService.findDashboardPosts();

        model.addAttribute("freePosts", postMapper.toResponseList(dashboardPosts.get(BoardType.FREE.getId())));
        model.addAttribute("galleryPosts", postMapper.toResponseList(dashboardPosts.get(BoardType.GALLERY.getId())));
        model.addAttribute("qnaPosts", postMapper.toResponseList(dashboardPosts.get(BoardType.QNA.getId())));
        model.addAttribute("noticePosts", postMapper.toResponseList(dashboardPosts.get(BoardType.NOTICE.getId())));

        return "admin/dashboard";
    }

    // 게시글 메인 페이지
    @GetMapping("/boards/{boardType}")
    public String boardList(@PathVariable String boardType, Model model, SearchRequest request) {

        model.addAttribute("search", request);

        BoardType type = BoardType.from(boardType);
        model.addAttribute("boardType", type);

        // TODO: Redis 적용 가능
        if (type.equals(BoardType.NOTICE)) {
            List<PostResponse> fixedPosts = postMapper.toResponseList(postService.findFixed());
            model.addAttribute("fixedPosts", fixedPosts);
        }

        List<CategoryVO> categories = categoryService.findAllByBoardType(type.getId());
        model.addAttribute("categories", categoryMapper.toResponseList(categories));

        SearchVO search = searchMapper.toVO(request, type.getId());

        int totalCount = postService.countBySearch(search);
        if (totalCount == 0) {
            model.addAttribute("posts", PostsResponse.empty());
        }

        List<PostResponse> posts = postMapper.toResponseList(postService.findAll(search));
        Pagination pagination = Pagination.of(request.page(), totalCount, request.size());

        model.addAttribute("posts", PostsResponse.of(posts, pagination));

        return "board/main";
    }

    // 게시글 등록 페이지
    @GetMapping("/boards/{boardType}/posts")
    public String writeForm(@PathVariable String boardType, Model model) {
        BoardType type = BoardType.from(boardType);
        model.addAttribute("boardType", type);

        if (type != BoardType.QNA) {
            List<CategoryVO> categories = categoryService.findAllByBoardType(type.getId());
            model.addAttribute("categories", categoryMapper.toResponseList(categories));
        }

        return "board/write";
    }

    // 게시글 수정 페이지
    @GetMapping("boards/{boardType}/posts/{postId}")
    public String editForm(
            @PathVariable String boardType,
            @PathVariable Long postId,
            Model model
    ) {
        BoardType type = BoardType.from(boardType);
        model.addAttribute("boardType", type);

        PostVO post = postService.findByIdWithDetail(postId);
        model.addAttribute("post", postMapper.toResponse(post));

        if (type != BoardType.QNA) {
            List<CategoryVO> categories = categoryService.findAllByBoardType(type.getId());
            model.addAttribute("categories", categoryMapper.toResponseList(categories));
        }

        return "board/write";
    }

    // 게시글 저장
    @PostMapping("boards/{boardType}/posts")
    public String save(
            @PathVariable String boardType,
            @Valid PostRequest request,
            @SessionAttribute("ADMIN_ID") Long adminId
    ) {
        BoardType type = BoardType.from(boardType);

        PostVO post = postService.save(postMapper.toVO(request, adminId, type.getId()));

        if (isFileSupportBoard(type)) {
            processFiles(request.files(), post.getId(), type);
        }

        return "redirect:/admin/boards/" + boardType;
    }

    // 게시글 수정
    @PostMapping("boards/{boardType}/posts/{postId}")
    public String update(
            @PathVariable String boardType,
            @PathVariable Long postId,
            @Valid PostRequest request,
            @SessionAttribute("ADMIN_ID") Long adminId
    ) {

        BoardType type = BoardType.from(boardType);

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
            processFiles(request.files(), postId, type);
        }

        return "redirect:/admin/boards/" + boardType;
    }

    // 게시글 삭제
    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<Void> deletePost(
            @PathVariable Long postId,
            @SessionAttribute("ADMIN_ID") Long adminId
    ) {

        postService.delete(postId);

        return ResponseEntity.ok().build();
    }

    // 댓글 + 답변 작성
    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<CommentResponse> saveComment(
            @PathVariable Long postId,
            @SessionAttribute("ADMIN_ID") Long adminId,
            @SessionAttribute("ADMIN_NAME") String adminName,
            @RequestBody @Valid CommentRequest request
    ) {

        CommentVO comment = commentService.saveComment(commentMapper.toVO(request, adminId, postId));
        comment.setNickname(adminName);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(commentMapper.toResponse(comment));
    }

    @PostMapping("/posts/{postId}/answer")
    public String saveAnswer(
            @PathVariable Long postId,
            @SessionAttribute("ADMIN_ID") Long adminId,
            @RequestBody @Valid CommentRequest request
    ) {
        commentService.saveComment(commentMapper.toVO(request, adminId, postId));

        return "redirect:/admin/boards/qna";
    }


    // 댓글 삭제
    @DeleteMapping("/posts/{postId}/comments/{commentId}")
    public ResponseEntity<Boolean> deleteComment(@PathVariable Long postId, @PathVariable Long commentId) {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(commentService.deleteById(commentId));
    }

    // 첨부파일 다운로드
    /**
     * 첨부파일 다운로드
     */
    @GetMapping("files/{fileId}/download")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long fileId) {

        try {
            FileVO file = fileService.findById(fileId);
            String objectKey = file.getSavedName();

            String encodedFileName = URLEncoder.encode(file.getOriginalName(), StandardCharsets.UTF_8.toString())
                    .replace("\\+", "%20");

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .contentLength(file.getFileSize())
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''" + encodedFileName)
                    .body(fileUtils.readFileAsResource(objectKey));

        } catch (Exception e) {
            throw ErrorCode.INTERNAL_SERVER_ERROR.defaultException(e);
        }
    }

    // s3이미지 url 가져오는 메서드
    @GetMapping("/files/{fileId}/display")
    public ResponseEntity<String> getImageUrl(@PathVariable Long fileId) {
        FileVO file = fileService.findById(fileId);
        String objectKey = file.getSavedName();
        String preSignedUrl = fileUtils.generatePresignedUrl(objectKey);

        return ResponseEntity.ok(preSignedUrl);
    }

    // 썸네일 url s3에서 가져오는 메서드
    @GetMapping("/posts/{postId}/thumbnail")
    public ResponseEntity<String> getThumbnailUrl(@PathVariable Long postId) {
        try {
            FileVO file = fileService.findThumbnailByPostId(postId);
            String objectKey = file.getSavedName();
            String preSignedUrl = fileUtils.generatePresignedUrl(objectKey);

            return ResponseEntity.ok(preSignedUrl);
        } catch (Exception e) {
            return ResponseEntity.ok("");
        }
    }

    // 파일 처리 해야하는 게시판인지 확인 메서드
    private boolean isFileSupportBoard(BoardType type) {
        return FILE_SUPPORT_BOARDS.contains(type);
    }

    /**
     * 파일 처리 함수
     * @param files multipartFile
     * @param postId 게시글 ID
     * @param type 게시판 타입
     */
    private void processFiles(List<MultipartFile> files, Long postId, BoardType type) {
        if (CollectionUtils.isEmpty(files) || files.get(0).isEmpty()) {
            return;
        }

        if (type.equals(BoardType.GALLERY) &&
                files.stream().anyMatch(f -> !Objects.requireNonNull(f.getContentType()).startsWith("image/"))
        ) {
            throw FileErrorCode.FILE_NOT_IMAGE.defaultException();
        }


        List<UploadedFile> uploadedFiles = fileUtils.uploadFiles(files);

        List<FileVO> fileVOList = fileMapper.toVOList(uploadedFiles, postId);

        fileService.saveFiles(fileVOList);


        if (type.equals(BoardType.GALLERY)) {
            processThumbnail(postId);
        }
    }

    /**
     * 썸네일 처리
     * @param postId 게시글 ID
     */
    private void processThumbnail(Long postId) {
        if (!postService.hasThumbnail(postId)) {
            FileVO fileVo = fileService.findFirstByPostId(postId);

            UploadedFile thumbnail = fileUtils.uploadThumbnail(fileMapper.toUploadedFile(fileVo));

            log.info("Saving thumbnail to DB: {}", thumbnail);

            fileService.saveThumbnail(fileMapper.toThumbnailVO(thumbnail, postId, fileVo.getId()));
        }
    }
}
