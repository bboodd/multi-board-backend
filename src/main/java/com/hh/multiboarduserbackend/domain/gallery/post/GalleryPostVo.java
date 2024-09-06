package com.hh.multiboarduserbackend.domain.gallery.post;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
public class GalleryPostVo {

    private Long galleryPostId;
    private Long memberId;
    private Long galleryCategoryId;
    private String title;
    private String content;
    private int viewCnt;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private int deleteYn;
}
