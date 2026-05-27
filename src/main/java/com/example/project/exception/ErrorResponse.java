package com.example.project.exception;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ErrorResponse {

    private final int status;
    private final String code;
    private final String message;
    private final LocalDateTime timestamp;

    private ErrorResponse(ErrorCode errorCode) {
        this.status    = errorCode.getStatus();
        this.code      = errorCode.getCode();
        this.message   = errorCode.getMessage();
        this.timestamp = LocalDateTime.now();
    }

    private ErrorResponse(ErrorCode errorCode, String message) {
        this.status    = errorCode.getStatus();
        this.code      = errorCode.getCode();
        this.message   = message;
        this.timestamp = LocalDateTime.now();
    }

    public static ErrorResponse of(ErrorCode errorCode) {
        return new ErrorResponse(errorCode);
    }

    public static ErrorResponse of(ErrorCode errorCode, String detail) {
        return new ErrorResponse(errorCode, detail);
    }
}
