package com.project.clickit.exceptions.login;

import com.project.clickit.exceptions.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class ConcurrentlySignUpException extends RuntimeException{
    private final HttpStatus httpStatus = ErrorCode.CONCURRENTLY_SIGNUP.getHttpStatus();
    private final String message = ErrorCode.CONCURRENTLY_SIGNUP.getMessage();
}
