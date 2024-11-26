package com.spring.multiboardbackend.global.util;

import com.spring.multiboardbackend.domain.post.enums.FileType;

public record UploadedFile(
        String originalName,
        String savedName,
        String savedPath,
        long fileSize,
        FileType fileType,
        String contentType
) {
    public static UploadedFile of(
            String originalName,
            String savedName,
            String savedPath,
            long fileSize,
            FileType fileType,
            String contentType
    ) {
        return new UploadedFile(
                originalName,
                savedName,
                savedPath,
                fileSize,
                fileType,
                contentType
        );
    }
}
