package com.hh.multiboarduserbackend.domain.ask.post;

import com.hh.multiboarduserbackend.common.vo.PostVo;
import com.hh.multiboarduserbackend.common.vo.SearchVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class AskPostService {

    private final AskPostRepository askPostRepository;

    /**
     * 게시글 저장후 생성된 키 반환
     * @param postVo - 게시글 정보
     * @return - ask_post_id(PK)
     */
    public Long savePost(PostVo postVo) {
        askPostRepository.save(postVo);
        return postVo.getPostId();
    }

    /**
     * 게시글 단건 조회
     * @param askPostId - 게시글 아이디
     * @return - dto
     */
    public Optional<PostVo> findById(Long askPostId) {
        return Optional.ofNullable(askPostRepository.findById(askPostId));
    }

    /**
     * 게시글 업데이트
     * @param postVo - 업데이트 정보
     * @return - pk
     */
    public Long updatePost(PostVo postVo){
        askPostRepository.update(postVo);
        return postVo.getPostId();
    }

    /**
     * 게시글 삭제
     * @param askPostId - pk
     * @return - pk
     */
    public Long deletePostById(Long askPostId) {
        askPostRepository.deleteById(askPostId);
        return askPostId;
    }

    /**
     * 검색 게시글 수 카운팅
     * @param searchVo - 검색 정보
     * @return - count
     */
    public int countAllBySearch(SearchVo searchVo) {
        return askPostRepository.countAllBySearch(searchVo);
    }

    /**
     * 검색 게시글 리스트 조회
     * @param searchVo - 검색 정보
     * @return - 게시글 리스트
     */
    public List<PostVo> findAllBySearch(SearchVo searchVo) {
        List<PostVo> postList = askPostRepository.findAllBySearch(searchVo);
        return postList;
    }

    /**
     * 게시글 조회수 1 증가
     * @param askPostId - 게시글 번호
     * @return - pk
     */
    public Long increaseViewCntById(Long askPostId) {
        askPostRepository.increaseViewCntById(askPostId);
        return askPostId;
    }
}
