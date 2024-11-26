package com.spring.multiboardbackend.domain.post.service;

import com.spring.multiboardbackend.domain.board.enums.BoardType;
import com.spring.multiboardbackend.domain.member.service.AuthService;
import com.spring.multiboardbackend.domain.post.dto.request.PostRequest;
import com.spring.multiboardbackend.domain.post.dto.response.FileResponse;
import com.spring.multiboardbackend.domain.post.dto.response.PostResponse;
import com.spring.multiboardbackend.domain.post.dto.response.PostsResponse;
import com.spring.multiboardbackend.domain.post.exception.PostErrorCode;
import com.spring.multiboardbackend.domain.post.mapper.PostMapper;
import com.spring.multiboardbackend.domain.post.repository.PostRepository;
import com.spring.multiboardbackend.domain.post.vo.PostVO;
import com.spring.multiboardbackend.global.common.mapper.SearchMapper;
import com.spring.multiboardbackend.global.common.request.SearchRequest;
import com.spring.multiboardbackend.global.common.response.Pagination;
import com.spring.multiboardbackend.global.common.vo.SearchVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;
    private final PostMapper postMapper;
    private final SearchMapper searchMapper;
    private final AuthService authService;
    private final FileService fileService;

    private static final List<BoardType> FILE_SUPPORT_BOARDS = List.of(BoardType.FREE, BoardType.GALLERY);

    /**
     * 게시글 등록 처리
     *
     * @param request 게시글 등록 정보
     * @param memberId 작성자 ID
     * @param type 게시판 타입
     * @return 등록된 게시글 Response
     */
    @Transactional
    public PostResponse save(PostRequest request, Long memberId, BoardType type) {
        PostVO post = postMapper.toVO(request, memberId, type.getId());
        postRepository.save(post);

        if (isFileSupportBoard(type)) {
            List<FileResponse> savedFiles = fileService.saveFiles(request.files(), post.getId(), type);
            return postMapper.toResponseForCreateAndUpdate(post, savedFiles);
        }

        return postMapper.toResponse(post);
    }

    /**
     * ID로 게시글 상세 조회
     *
     * @param id 조회할 게시글 ID
     * @return 게시글 상세 정보 Response
     */
    public PostResponse findById(Long id) {
        return postRepository.findByIdWithDetail(id)
                .map(postMapper::toResponse)
                .orElseThrow(PostErrorCode.POST_NOT_FOUND::defaultException);
    }

    /**
     * 게시글 수정 처리
     *
     * @param request 게시글 수정 정보
     * @param memberId 수정 요청자 ID
     * @param postId 수정할 게시글 ID
     * @param type 게시판 타입
     * @return 수정된 게시글 Response
     */
    @Transactional
    public PostResponse update(PostRequest request, Long memberId, Long postId, BoardType type) {
        PostVO post = postRepository.findById(postId)
                .orElseThrow(PostErrorCode.POST_NOT_FOUND::defaultException);

        if (!post.getMemberId().equals(memberId) && !authService.isAdmin(memberId)) {
            throw PostErrorCode.PERMISSION_DENIED.defaultException();
        }

        PostVO updatedPost = postMapper.toVOForUpdate(request, postId);
        postRepository.update(updatedPost);

        if (isFileSupportBoard(type)) {
            List<FileResponse> files = fileService.updateFiles(request.removeFileIds(), request.files(), postId, type);
            return postMapper.toResponseForCreateAndUpdate(updatedPost, files);
        }

        return postMapper.toResponse(updatedPost);
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
     * 게시글 삭제 처리
     *
     * @param id 삭제할 게시글 ID
     * @param memberId 삭제 요청자 ID
     * @return 삭제 성공 여부
     */
    @Transactional
    public boolean delete(Long id, Long memberId) {
        PostVO post = postRepository.findById(id)
                .orElseThrow(PostErrorCode.POST_NOT_FOUND::defaultException);

        if (!post.getMemberId().equals(memberId) && !authService.isAdmin(memberId)) {
            throw PostErrorCode.PERMISSION_DENIED.defaultException();
        }

        postRepository.deleteById(id);

        return true;
    }

    /**
     * 게시글 목록 조회
     *
     * @param request 검색 조건
     * @param type 게시판 타입
     * @return 게시글 목록 Response
     */
    public PostsResponse findAll(SearchRequest request, BoardType type) {
        SearchVO search = searchMapper.toVO(request, type.getId());

        int totalCount = postRepository.countBySearch(search);
        if (totalCount == 0) {
            return PostsResponse.empty();
        }

        List<PostResponse> posts = postRepository.findAllBySearch(search)
                .stream()
                .map(postMapper::toResponse)
                .toList();

        Pagination pagination = Pagination.of(request.page(), totalCount, request.size());

        return PostsResponse.of(posts, pagination);
    }

    /**
     * 게시글 조회수 증가
     *
     * @param id 조회수 증가할 게시글 ID
     */
    @Transactional
    public void incrementViewCount(Long id) {
        postRepository.findById(id)
                .orElseThrow(PostErrorCode.POST_NOT_FOUND::defaultException);

        postRepository.incrementViewCount(id);
    }
}