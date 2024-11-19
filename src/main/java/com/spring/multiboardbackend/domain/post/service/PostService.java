package com.spring.multiboardbackend.domain.post.service;

import com.spring.multiboardbackend.domain.post.repository.PostRepository;
import com.spring.multiboardbackend.domain.post.vo.PostVo;
import com.spring.multiboardbackend.global.common.vo.SearchVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class PostService {

    private final PostRepository postRepository;

    /**
     * 게시글 저장후 생성된 키 반환
     * @param postVo - 게시글 정보
     * @return - _post_id(PK)
     */
    public Long savePost(PostVo postVo) {
        postRepository.save(postVo);
        return postVo.getPostId();
    }

    /**
     * 게시글 단건 조회
     * @param postId - 게시글 아이디
     * @return - dto
     */
    public Optional<PostVo> findById(Long postId) {
        return Optional.ofNullable(postRepository.findById(postId));
    }

    /**
     * 게시글 업데이트
     * @param postVo - 업데이트 정보
     * @return - pk
     */
    public Long updatePost(PostVo postVo){
        postRepository.update(postVo);
        return postVo.getPostId();
    }

    /**
     * 게시글 삭제
     * @param postId - pk
     * @return - pk
     */
    public Long deletePostById(Long postId) {
        postRepository.deleteById(postId);
        return postId;
    }

    /**
     * 검색 게시글 수 카운팅
     * @param searchVo - 검색 정보
     * @return - count
     */
    public int countAllBySearch(SearchVo searchVo) {
        return postRepository.countAllBySearch(searchVo);
    }

    /**
     * 검색 게시글 리스트 조회
     * @param searchVo - 검색 정보
     * @return - 게시글 리스트
     */
    public List<PostVo> findAllBySearch(SearchVo searchVo) {
        List<PostVo> postList = postRepository.findAllBySearch(searchVo);
        return postList;
    }

    /**
     * 게시글 조회수 1 증가
     * @param postId - 게시글 번호
     * @return - pk
     */
    public Long increaseViewCntById(Long postId) {
        postRepository.increaseViewCntById(postId);
        return postId;
    }
}
