package com.hh.multiboarduserbackend.common.exception;

public class CustomException extends RuntimeException{

    protected ErrorCode ERROR_CODE;

    private static ErrorCode getDefaultErrorCode() {
        return ErrorCode.DEFAULT_ERROR_CODE;
    }

    public CustomException() {
        this.ERROR_CODE = getDefaultErrorCode();
    }

    public CustomException(String message) {
        super(message);
        this.ERROR_CODE = getDefaultErrorCode();
    }

    public CustomException(String message, Throwable cause) {
        super(message, cause);
        this.ERROR_CODE = getDefaultErrorCode();
    }

    public CustomException(ErrorCode errorCode) {
        super(errorCode.defaultMessage());
        this.ERROR_CODE = errorCode;
    }

    public CustomException(ErrorCode errorCode, Throwable cause) {
        super(errorCode.defaultMessage(), cause);
        this.ERROR_CODE = errorCode;
    }

    public ErrorCode getErrorCode() {
        return ERROR_CODE;
    }
}
