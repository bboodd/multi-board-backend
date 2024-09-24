package com.hh.multiboarduserbackend.domain.board;

import com.hh.multiboarduserbackend.exception.BoardErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum BoardType {

    FREE("자유게시판", "free", 1L),
    ASK("문의게시판", "ask", 2L),
    NOTICE("공지게시판", "notice", 3L),
    GALLERY("갤러리게시판", "gallery", 4L);

    private final String name;
    private final String typeName;
    private final Long typeId;

    public static BoardType of(String typeName) {
        return Arrays.stream(BoardType.values())
                .filter(boardType -> typeName.equals(boardType.typeName))
                .findFirst()
                .orElseThrow(BoardErrorCode.BOARD_NOT_FOUND::defaultException);
    }

    public static Long getTypeId(String path) {
        BoardType boardType = BoardType.of(path);
        return boardType.typeId;
    }
}
