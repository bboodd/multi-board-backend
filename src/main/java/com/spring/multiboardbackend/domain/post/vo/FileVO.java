package com.spring.multiboardbackend.domain.post.vo;

import com.spring.multiboardbackend.domain.post.enums.FileType;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class FileVO {
    private Long id;
    private Long postId;
    private String originalName;
    private String savedName;
    private String savedPath;
    private Long fileSize;
    private FileType fileType;
    private String contentType;
    private LocalDateTime createdAt;

    public String getFullPath() {
        return savedPath + "/" + savedName;
    }
}
