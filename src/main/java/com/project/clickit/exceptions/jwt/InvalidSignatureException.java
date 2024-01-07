package com.project.clickit.exceptions.jwt;

import com.project.clickit.exceptions.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class InvalidSignatureException extends RuntimeException{
    private final HttpStatus httpStatus = ErrorCode.INVALID_SIGNATURE_TOKEN.getHttpStatus();
    private final String message = ErrorCode.INVALID_SIGNATURE_TOKEN.getMessage();
}
