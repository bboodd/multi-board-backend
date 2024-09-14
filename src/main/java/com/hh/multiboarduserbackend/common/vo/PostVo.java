package com.hh.multiboarduserbackend.common.vo;

import com.hh.multiboarduserbackend.common.dto.request.PostRequestDto;
import com.hh.multiboarduserbackend.mappers.PostMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostVo {

    private Long postId;            // pk
    private Long memberId;              // fk
    private Long categoryId;        // fk
    private String title;               // 제목
    private String content;             // 내용
    private int viewCnt;                // 조회수
    private LocalDateTime createdDate;  // 생성일시
    private LocalDateTime updatedDate;  // 수정일시
    private int deleteYn;               // 삭제 1 미삭제 0
    private String categoryName;    // 서브쿼리문
    private String nickname;            // 서브쿼리문
    private int fileCount;              // 서브쿼리문

    public static PostVo toVo(PostRequestDto postRequestDto, Long memberId) {
        return PostMapper.INSTANCE.toVo(postRequestDto, memberId);
    }
}
