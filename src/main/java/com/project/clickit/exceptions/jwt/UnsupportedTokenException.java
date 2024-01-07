package com.project.clickit.exceptions.jwt;

import com.project.clickit.exceptions.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class UnsupportedTokenException extends RuntimeException{
    private final HttpStatus httpStatus = ErrorCode.UNSUPPORTED_TOKEN.getHttpStatus();
    private final String message = ErrorCode.UNSUPPORTED_TOKEN.getMessage();
}
