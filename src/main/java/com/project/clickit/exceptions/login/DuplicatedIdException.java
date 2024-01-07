package com.project.clickit.exceptions.login;

import com.project.clickit.exceptions.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class DuplicatedIdException extends RuntimeException{
    private final HttpStatus httpStatus = ErrorCode.DUPLICATED_ID.getHttpStatus();
    private final String message = ErrorCode.DUPLICATED_ID.getMessage();
}
