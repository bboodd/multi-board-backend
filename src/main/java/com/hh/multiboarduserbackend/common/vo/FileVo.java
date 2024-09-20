package com.hh.multiboarduserbackend.common.vo;

import com.hh.multiboarduserbackend.common.dto.request.FileRequestDto;
import com.hh.multiboarduserbackend.mappers.FileMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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
