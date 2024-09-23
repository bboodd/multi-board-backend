package com.hh.multiboarduserbackend.domain.notice.post;

import com.hh.multiboarduserbackend.common.dto.SearchDto;
import com.hh.multiboarduserbackend.common.dto.response.PostResponseDto;
import com.hh.multiboarduserbackend.common.paging.PaginationDto;
import com.hh.multiboarduserbackend.common.paging.PagingAndListResponse;
import com.hh.multiboarduserbackend.common.response.Response;
import com.hh.multiboarduserbackend.common.utils.PaginationUtils;
import com.hh.multiboarduserbackend.common.vo.PostVo;
import com.hh.multiboarduserbackend.common.vo.SearchVo;
import com.hh.multiboarduserbackend.exception.PostErrorCode;
import com.hh.multiboarduserbackend.mappers.PostMapper;
import com.hh.multiboarduserbackend.mappers.SearchMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api-board/notice-board")
public class NoticePostController {

    private final NoticePostService noticePostService;
    private final PaginationUtils paginationUtils;
    private final PostMapper postModelMapper = Mappers.getMapper(PostMapper.class);
    private final SearchMapper searchModelMapper = Mappers.getMapper(SearchMapper.class);

    // 게시글 리스트 조회
    @GetMapping("/posts")
    public ResponseEntity<?> getPosts(SearchDto searchDto) {

        PagingAndListResponse<PostResponseDto> pagingAndListResponse;

        SearchVo searchVo = searchModelMapper.toVo(searchDto);

        int count = noticePostService.countAllBySearch(searchVo);

        if(count >= 1) {
            PaginationDto paginationDto = paginationUtils.createPagination(count ,searchDto);

            SearchVo searchVoWithPagination = searchModelMapper.toVoWithPagination(searchDto, paginationDto);
            List<PostVo> postVoList = noticePostService.findAllBySearch(searchVoWithPagination);

            List<PostResponseDto> postResponseDtoList = postModelMapper.toDtoList(postVoList);

            pagingAndListResponse = new PagingAndListResponse<>(postResponseDtoList, paginationDto);
        } else {
            pagingAndListResponse = new PagingAndListResponse<>(Collections.emptyList(), null);
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(Response.data(pagingAndListResponse));
    }

    // 공지글 조회
    @GetMapping("/posts/fin")
    public ResponseEntity<?> getFinPosts() {

        List<PostVo> postVoList = noticePostService.findAllAsFin();
        List<PostResponseDto> postResponseDtoList = postModelMapper.toDtoList(postVoList);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(Response.data(postResponseDtoList));
    }

    // 게시글 조회
    @GetMapping("/posts/{noticePostId}")
    public ResponseEntity<?> getPost(@PathVariable Long noticePostId) {

        noticePostService.increaseViewCntById(noticePostId);

        PostVo postVo = noticePostService.findById(noticePostId).orElseThrow(PostErrorCode.POST_NOT_FOUND::defaultException);

        PostResponseDto postResponseDto = postModelMapper.toDto(postVo);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(Response.data(postResponseDto));
    }
}
