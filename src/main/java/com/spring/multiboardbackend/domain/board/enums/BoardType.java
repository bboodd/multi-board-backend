package com.spring.multiboardbackend.domain.board.enums;

import com.spring.multiboardbackend.domain.board.exception.BoardErrorCode;
import com.spring.multiboardbackend.global.exception.CustomException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public enum BoardType {

    FREE("자유게시판", "free", 1L),
    QNA("문의게시판", "qna", 2L),
    NOTICE("공지게시판", "notice", 3L),
    GALLERY("갤러리게시판", "gallery", 4L);

    private static final Map<String, BoardType> TYPE_MAP = Arrays.stream(values())
            .collect(Collectors.toUnmodifiableMap(
                    BoardType::getTypeName,
                    Function.identity()
            ));


    private final String name;
    private final String typeName;
    private final Long id;

    /**
     * 게시판 타입명으로 BoardType을 찾습니다.
     *
     * @param typeName 게시판 타입명
     * @return 해당하는 BoardType
     * @throws CustomException 해당하는 게시판 타입이 없을 경우
     */
    public static BoardType from(String typeName) {
        return Optional.ofNullable(TYPE_MAP.get(typeName))
                .orElseThrow(BoardErrorCode.BOARD_NOT_FOUND::defaultException);
    }

    /**
     * 게시판 타입이 유효한지 확인합니다.
     *
     * @param typeName 게시판 타입명
     * @return 유효성 여부
     */
    public static boolean isValid(String typeName) {
        return TYPE_MAP.containsKey(typeName);
    }

    @Override
    public String toString() {
        return String.format("%s(%s)", name, typeName);
    }
}
