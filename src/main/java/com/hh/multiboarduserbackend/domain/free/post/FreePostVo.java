package com.hh.multiboarduserbackend.domain.free.post;

import com.hh.multiboarduserbackend.mappers.PostMapper;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
public class FreePostVo {

    private Long freePostId;            // pk
    private Long memberId;              // fk
    private Long freeCategoryId;        // fk
    private String title;               // 제목
    private String content;             // 내용
    private int viewCnt;                // 조회수
    private LocalDateTime createdDate;  // 생성일시
    private LocalDateTime updatedDate;  // 수정일시
    private int deleteYn;               // 삭제 1 미삭제 0
    private String freeCategoryName;    // 서브쿼리문
    private int fileCount;              // 서브쿼리문
    private String nickname;            // 서브쿼리문

    public static FreePostVo toVo(FreePostRequestDto freePostRequestDto, Long memberId) {
        return PostMapper.INSTANCE.toVo(freePostRequestDto, memberId);
    }
}
