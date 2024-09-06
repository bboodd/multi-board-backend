package com.hh.multiboarduserbackend.domain.ask.post;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
public class AskPostVo {

    private Long askPostId;
    private Long memberId;
    private String title;
    private String content;
    private int viewCnt;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private int deleteYn;
    private int lockYn;
}
