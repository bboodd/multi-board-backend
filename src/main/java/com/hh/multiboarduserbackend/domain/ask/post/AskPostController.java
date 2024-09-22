package com.hh.multiboarduserbackend.domain.ask.post;

import com.hh.multiboarduserbackend.aop.AuthenticationContextHolder;
import com.hh.multiboarduserbackend.common.dto.SearchDto;
import com.hh.multiboarduserbackend.common.dto.request.PostRequestDto;
import com.hh.multiboarduserbackend.common.dto.response.PostResponseDto;
import com.hh.multiboarduserbackend.common.paging.PaginationDto;
import com.hh.multiboarduserbackend.common.paging.PagingAndListResponse;
import com.hh.multiboarduserbackend.common.response.Response;
import com.hh.multiboarduserbackend.common.utils.PaginationUtils;
import com.hh.multiboarduserbackend.common.vo.PostVo;
import com.hh.multiboarduserbackend.common.vo.SearchVo;
import com.hh.multiboarduserbackend.domain.member.LoginMember;
import com.hh.multiboarduserbackend.exception.PostErrorCode;
import com.hh.multiboarduserbackend.mappers.PostMapper;
import com.hh.multiboarduserbackend.mappers.SearchMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;

@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api-board/ask-board")
public class AskPostController {

    private final AskPostService askPostService;
    private final PaginationUtils paginationUtils;
    private final PostMapper postModelMapper = Mappers.getMapper(PostMapper.class);
    private final SearchMapper searchModelMapper = Mappers.getMapper(SearchMapper.class);

    // 게시글 리스트 조회
    @GetMapping("/posts")
    public ResponseEntity<?> getPosts(SearchDto searchDto) {

        PagingAndListResponse<PostResponseDto> pagingAndListResponse;

        SearchVo searchVo = searchModelMapper.toVo(searchDto);

        int count = askPostService.countAllBySearch(searchVo);

        if(count >= 1) {
            PaginationDto paginationDto = paginationUtils.createPagination(count ,searchDto);

            SearchVo searchVoWithPagination = searchModelMapper.toVoWithPagination(searchDto, paginationDto);
            List<PostVo> postVoList = askPostService.findAllBySearch(searchVoWithPagination);

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
    @GetMapping("/posts/{askPostId}")
    public ResponseEntity<?> getPost(@PathVariable Long askPostId) {

        askPostService.increaseViewCntById(askPostId);

        PostVo postVo = askPostService.findById(askPostId).orElseThrow(PostErrorCode.POST_NOT_FOUND::defaultException);

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
        final Long askPostId = askPostService.savePost(postVo);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(Response.message(askPostId + "번 게시글 등록완료"));
    }

    // 게시글 수정
    @LoginMember
    @PutMapping("/posts/{askPostId}")
    public ResponseEntity<?> updatePost(@PathVariable Long askPostId, @Valid PostRequestDto postRequestDto) {

        final Long memberId = AuthenticationContextHolder.getContext();

        PostVo postVo = postModelMapper.toVoWithMemberId(postRequestDto, memberId);

        // 수정
        askPostService.updatePost(postVo);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(Response.message(askPostId + "번 게시글 수정 완료"));
    }

    // 게시글 삭제
    @LoginMember
    @DeleteMapping("/posts/{askPostId}")
    public ResponseEntity<?> deletePost(@PathVariable Long askPostId) {

        final Long memberId = AuthenticationContextHolder.getContext();

        askPostService.deletePostById(askPostId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(Response.message(askPostId + "번 게시글 삭제 완료"));
    }

}
