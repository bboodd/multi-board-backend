package com.hh.multiboarduserbackend.domain.gallery.post;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record GalleryPostVo(
          Long galleryPostId
        , Long memberId
        , Long galleryCategoryId
        , String title
        , String content
        , int viewCnt
        , LocalDateTime createdDate
        , LocalDateTime updatedDate
        , int deleteYn
) {
}
