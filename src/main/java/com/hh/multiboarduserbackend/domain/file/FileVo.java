package com.hh.multiboarduserbackend.domain.file;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileVo {

    private Long fileId;            // pk
    private Long postId;            // kf
    private String originalName;    // 원본이름
    private String savedName;       // 저장된이름 UUID
    private String savedPath;       // 저장된 경로
    private long savedSize;         // 저장된 크기
    private int deleteYn;           // 삭제시 1 미삭제 0
}
