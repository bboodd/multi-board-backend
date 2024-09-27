package com.hh.multiboarduserbackend.aop;

import com.hh.multiboarduserbackend.exception.CustomException;
import com.hh.multiboarduserbackend.common.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 커스텀 exception 한곳에서 처리
     * @param exception - custom exception
     * @return - ErrorResponse
     */
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException exception) {
        return ResponseEntity
                .status(exception.getErrorCode().defaultHttpStatus())
                .body(ErrorResponse.of(exception));
    }

    /**
     * 400에러
     * 요청 객체의 validation을 수행할 때, MethodArgumentNotValidException이 발생
     * 각 검증 어노테이션 별로 지정해놨던 메시지를 응답
     * @param e - 에러
     * @return 400 + message
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidException(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldError().getDefaultMessage();
        ErrorResponse response = ErrorResponse.builder()
                .status(400)
                .code("BAD_REQUEST")
                .message(message)
                .build();
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }
}
