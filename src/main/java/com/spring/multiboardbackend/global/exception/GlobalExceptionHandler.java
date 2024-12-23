package com.spring.multiboardbackend.global.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * CustomException 처리
     */
    @ExceptionHandler(CustomException.class)
    public Object handleCustomException(HttpServletRequest request, Model model, CustomException e) {
        log.error("CustomException: {}", e.getMessage(), e);

        if (isApiRequest(request)) {
            return ResponseEntity
                    .status(e.getErrorCode().getStatus())
                    .body(ErrorResponse.of(e.getErrorCode()));
        }
        else {
            model.addAttribute("code", e.getErrorCode().getStatus().value());
            model.addAttribute("msg", e.getMessage());
            model.addAttribute("status", e.getErrorCode().getStatus());

            return new ModelAndView("error/error");
        }
    }

    /**
     * 요청 데이터 검증 실패 처리
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Object handleValidException(HttpServletRequest request, Model model, MethodArgumentNotValidException e) {

        List<Map<String, String>> errors = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> Map.of(
                        "field", error.getField()
                        , "message", Optional.ofNullable(error.getDefaultMessage())
                                .orElse("Invalid value")
                ))
                .toList();

        String message = Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage();
        log.error("Validation error: {}", errors);

        if (isApiRequest(request)) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ErrorResponse.of(
                            CommonErrorCode.INVALID_REQUEST,
                            "입력값이 올바르지 않습니다",
                            message
                    ));
        }
        else {
            model.addAttribute("code", HttpStatus.BAD_REQUEST.name());
            model.addAttribute("msg", message);
            model.addAttribute("status", HttpStatus.BAD_REQUEST.value());

            return new ModelAndView("error/error");
        }
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<String> handleNoResourceFound(NoResourceFoundException e) {
        log.warn("Resource not found: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Resource not found");
    }

    /**
     * 예상치 못한 예외 처리
     */
    @ExceptionHandler(Exception.class)
    public Object handleException(HttpServletRequest request, Model model, Exception e) {
        log.error("Unexpected error occurred: ", e);

        Map<String, String> errors = Map.of(
                "domain", "Exception",
                "reason", e.getClass().getSimpleName()
//                "message", e.getMessage() != null ? e.getMessage() : "Unknown error"
        );

        if (isApiRequest(request)) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ErrorResponse.of(
                            ErrorCode.INTERNAL_SERVER_ERROR,
                            "서버 오류가 발생했습니다",
                            errors
                    ));
        }
        else {
            model.addAttribute("code", HttpStatus.INTERNAL_SERVER_ERROR.name());
            model.addAttribute("msg", e.getClass().getSimpleName());
            model.addAttribute("status", HttpStatus.INTERNAL_SERVER_ERROR.value());

            return new ModelAndView("error/error");
        }
    }

    private boolean isApiRequest(HttpServletRequest request) {
        String uri = request.getRequestURI();
        return uri.startsWith("/api/");
    }
}
