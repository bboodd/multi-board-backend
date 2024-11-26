package com.spring.multiboardbackend.domain.post.dto.response;

import com.spring.multiboardbackend.domain.board.dto.response.CategoryResponse;
import com.spring.multiboardbackend.global.common.response.Pagination;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@Schema(description = "게시글 목록 응답")
public record PostsResponse(
        @Schema(description = "게시글 목록")
        List<PostResponse> data,
        @Schema(description = "페이지네이션")
        Pagination pagination
) {
        @Builder
        public PostsResponse {
            data = data != null ? data : new ArrayList<>();
        }

        /**
         * 응답 생성
         */
        public static PostsResponse of(
                List<PostResponse> data,
                Pagination pagination
        ) {
            return new PostsResponse(
                    data,
                    pagination
            );
        }

        /**
         * 빈 응답 생성
         */
        public static PostsResponse empty() {
            return new PostsResponse(
                    Collections.emptyList(),
                    Pagination.empty()
            );
        }


}
