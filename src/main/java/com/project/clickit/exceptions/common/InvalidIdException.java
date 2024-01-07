package com.project.clickit.exceptions.common;

import com.project.clickit.exceptions.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class InvalidIdException extends RuntimeException{
    private final HttpStatus httpStatus = ErrorCode.INVALID_ID.getHttpStatus();
    private final String message = ErrorCode.INVALID_ID.getMessage();
}
