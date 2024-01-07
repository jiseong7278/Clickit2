package com.project.clickit.exceptions.jwt;

import com.project.clickit.exceptions.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class InvalidIssuerException extends RuntimeException{
    private final HttpStatus httpStatus = ErrorCode.INVALID_ISSUER.getHttpStatus();
    private final String message = ErrorCode.INVALID_ISSUER.getMessage();
}
