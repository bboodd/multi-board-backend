package com.spring.multiboardbackend.global.util;


public record UploadedFile(
        String originalName,
        String savedName,
        String savedPath,
        long fileSize,
        String contentType
) {
    public static UploadedFile of(
            String originalName,
            String savedName,
            String savedPath,
            long fileSize,
            String contentType
    ) {
        return new UploadedFile(
                originalName,
                savedName,
                savedPath,
                fileSize,
                contentType
        );
    }
}
