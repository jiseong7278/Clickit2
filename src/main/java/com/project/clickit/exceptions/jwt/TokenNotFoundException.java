package com.project.clickit.exceptions.jwt;

import com.project.clickit.exceptions.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class TokenNotFoundException extends RuntimeException{
    private final HttpStatus httpStatus = ErrorCode.TOKEN_NOT_FOUND.getHttpStatus();
    private final String message = ErrorCode.TOKEN_NOT_FOUND.getMessage();
}
