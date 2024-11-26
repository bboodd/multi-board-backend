package com.spring.multiboardbackend.domain.board.vo;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CategoryVO {
    private Long id;
    private String name;
    private Long boardTypeId;
}
