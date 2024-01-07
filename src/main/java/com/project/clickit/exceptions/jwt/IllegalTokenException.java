package com.project.clickit.exceptions.jwt;

import com.project.clickit.exceptions.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class IllegalTokenException extends RuntimeException{
    private final HttpStatus httpStatus = ErrorCode.ILLEGAL_TOKEN.getHttpStatus();
    private final String message = ErrorCode.ILLEGAL_TOKEN.getMessage();
}
