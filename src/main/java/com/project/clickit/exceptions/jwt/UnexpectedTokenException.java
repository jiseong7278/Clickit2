package com.project.clickit.exceptions.jwt;

import com.project.clickit.exceptions.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class UnexpectedTokenException extends RuntimeException{
    private final HttpStatus httpStatus = ErrorCode.UNEXPECTED_TOKEN_ERROR.getHttpStatus();
    private final String message = ErrorCode.UNEXPECTED_TOKEN_ERROR.getMessage();
}
