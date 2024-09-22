package com.hh.multiboarduserbackend.domain.notice.post;

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
public class NoticePostService {

    private final NoticePostRepository noticePostRepository;

    /**
     * 게시글 단건 조회
     * @param noticePostId - 게시글 아이디
     * @return - dto
     */
    public Optional<PostVo> findById(Long noticePostId) {
        return Optional.ofNullable(noticePostRepository.findById(noticePostId));
    }

    /**
     * 검색 게시글 수 카운팅
     * @param searchVo - 검색 정보
     * @return - count
     */
    public int countAllBySearch(SearchVo searchVo) {
        return noticePostRepository.countAllBySearch(searchVo);
    }

    /**
     * 검색 게시글 리스트 조회
     * @param searchVo - 검색 정보
     * @return - 게시글 리스트
     */
    public List<PostVo> findAllBySearch(SearchVo searchVo) {
        List<PostVo> postList = noticePostRepository.findAllBySearch(searchVo);
        return postList;
    }

    /**
     * 게시글 조회수 1 증가
     * @param noticePostId - 게시글 번호
     * @return - pk
     */
    public Long increaseViewCntById(Long noticePostId) {
        noticePostRepository.increaseViewCntById(noticePostId);
        return noticePostId;
    }
}
