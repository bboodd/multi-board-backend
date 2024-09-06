package com.hh.multiboarduserbackend.domain.free.post;

import com.hh.multiboarduserbackend.common.paging.PagingAndListResponse;
import com.hh.multiboarduserbackend.common.vo.SearchVo;
import com.hh.multiboarduserbackend.exception.PostErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.hh.multiboarduserbackend.domain.free.post.FreePostResponseDto.toDto;
import static java.util.stream.Collectors.toList;

@Slf4j
@RequiredArgsConstructor
@Service
public class FreePostService {

    private final FreePostRepository freePostRepository;

    /**
     * 게시글 저장후 생성된 키 반환
     * @param freePostVo - 게시글 정보
     * @return - free_post_id(PK)
     */
    public Long savePost(FreePostVo freePostVo) {
        freePostRepository.save(freePostVo);
        return freePostVo.getFreePostId();
    }

    /**
     * 게시글 단건 조회
     * @param freePostId - 게시글 아이디
     * @return - dto
     */
    public FreePostResponseDto findById(Long freePostId) {
        Optional<FreePostVo> post = Optional.ofNullable(freePostRepository.findById(freePostId));
        return toDto(post.orElseThrow(() -> PostErrorCode.POST_NOT_FOUND.defaultException()));
    }

    /**
     * 게시글 업데이트
     * @param freePostVo - 업데이트 정보
     * @return - pk
     */
    public Long updatePost(FreePostVo freePostVo){
        freePostRepository.update(freePostVo);
        return freePostVo.getFreePostId();
    }

    /**
     * 게시글 삭제
     * @param freePostId - pk
     * @return - pk
     */
    public Long deletePostById(Long freePostId) {
        freePostRepository.deleteById(freePostId);
        return freePostId;
    }

    /**
     * 검색 게시글 수 카운팅
     * @param searchVo - 검색 정보
     * @return - count
     */
    public int countAllBySearch(SearchVo searchVo) {
        return freePostRepository.countAllBySearch(searchVo);
    }

    /**
     * 검색 게시글 리스트 조회
     * @param searchVo - 검색 정보
     * @return - 게시글 리스트
     */
    public List<FreePostResponseDto> findAllBySearch(SearchVo searchVo) {
        List<FreePostVo> postList = freePostRepository.findAllBySearch(searchVo);

        List<FreePostResponseDto> dtoList = postList.stream()
                .map(FreePostResponseDto::toDto).collect(toList());
        return dtoList;
    }

    /**
     * 게시글 조회수 1 증가
     * @param freePostId - 게시글 번호
     * @return - pk
     */
    public Long increaseViewCntById(Long freePostId) {
        freePostRepository.increaseViewCntById(freePostId);
        return freePostId;
    }
}
