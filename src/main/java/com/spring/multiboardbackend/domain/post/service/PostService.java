package com.spring.multiboardbackend.domain.post.service;

import com.spring.multiboardbackend.domain.post.exception.PostErrorCode;
import com.spring.multiboardbackend.domain.post.repository.PostRepository;
import com.spring.multiboardbackend.domain.post.vo.PostVO;
import com.spring.multiboardbackend.global.common.vo.SearchVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;

    /**
     * 새로운 게시글을 등록합니다.
     *
     * @param post 저장할 게시글 정보가 담긴 PostVO
     * @return 저장된 게시글 정보 (생성된 ID 포함)
     */
    @Transactional
    public PostVO save(PostVO post) {
        postRepository.save(post);

        return post;
    }

    /**
     * 게시글 ID로 상세 정보를 조회합니다.
     *
     * @param id 조회할 게시글의 ID
     * @return 게시글의 상세 정보가 담긴 PostVO
     */
    public PostVO findByIdWithDetail(Long id) {
        return postRepository.findByIdWithDetail(id)
                .orElseThrow(PostErrorCode.POST_NOT_FOUND::defaultException);
    }

    /**
     * 게시글 ID로 기본 정보를 조회합니다.
     *
     * @param id 조회할 게시글의 ID
     * @return 게시글의 기본 정보가 담긴 PostVO
     */
    public PostVO findById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(PostErrorCode.POST_NOT_FOUND::defaultException);
    }

    /**
     * 기존 게시글을 수정합니다.
     *
     * @param post 수정할 내용이 담긴 PostVO
     * @return 수정된 게시글 정보
     */
    @Transactional
    public PostVO update(PostVO post) {
        postRepository.update(post);

        return post;
    }

    /**
     * 게시글을 삭제합니다.
     *
     * @param id 삭제할 게시글의 ID
     * @return 삭제 성공 여부
     */
    @Transactional
    public boolean delete(Long id) {
        postRepository.deleteById(id);

        return true;
    }

    /**
     * 검색 조건에 맞는 게시글 목록을 조회합니다.
     *
     * @param search 검색 조건이 담긴 SearchVO
     * @return 검색 조건에 맞는 게시글 목록
     */
    public List<PostVO> findAll(SearchVO search) {
        return postRepository.findAllBySearch(search);
    }

    /**
     * 게시글의 조회수를 증가시킵니다.
     *
     * @param id 조회수를 증가시킬 게시글의 ID
     */
    @Transactional
    public void incrementViewCount(Long id) {
        if (postRepository.findById(id).isPresent()) {
            postRepository.incrementViewCount(id);
        } else {
            throw PostErrorCode.POST_NOT_FOUND.defaultException();
        }
    }

    /**
     * 검색 조건에 맞는 게시글의 총 개수를 조회합니다.
     *
     * @param search 검색 조건이 담긴 SearchVO
     * @return 검색 조건에 맞는 게시글의 총 개수
     */
    public int countBySearch(SearchVO search) {
        return postRepository.countBySearch(search);
    }

    /**
     * 게시글의 썸네일 이미지 존재 여부를 확인합니다.
     *
     * @param id 확인할 게시글의 ID
     * @return 썸네일 존재 여부 (있으면 true, 없으면 false)
     */
    public boolean hasThumbnail(Long id) {
        return postRepository.existsThumbnailById(id);
    }

    public List<PostVO> findFixed() {
        return postRepository.findAllWithFixed();
    }

    public Map<Long, List<PostVO>> findDashboardPosts() {
        return postRepository.findDashboardPosts().stream()
                .collect(Collectors.groupingBy(
                        PostVO::getBoardTypeId,
                        Collectors.toList()
                ));
    }
}