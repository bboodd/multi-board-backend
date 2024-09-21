package com.hh.multiboarduserbackend.domain.gallery.thumbnail;


import com.hh.multiboarduserbackend.common.vo.FileVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class GalleryThumbnailService {

    private final GalleryThumbnailRepository galleryThumbnailRepository;

    /**
     * 썸네일 저장
     * @param fileVo - 썸네일 파일 정보
     */
    public void saveThumbnail(FileVo fileVo) {
        galleryThumbnailRepository.save(fileVo);
    }

    /**
     * 게시글의 썸네일 존재하는지 확인하는 메서드
     * @param galleryPostId - postId
     * @return - true or false
     */
    public boolean checkExistsThumbnail(Long galleryPostId) {
        int count = galleryThumbnailRepository.countAllByPostId(galleryPostId);
        return count != 0;
    }
}
