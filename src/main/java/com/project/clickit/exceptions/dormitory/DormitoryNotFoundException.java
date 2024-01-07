package com.project.clickit.exceptions.dormitory;

import com.project.clickit.exceptions.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class DormitoryNotFoundException extends RuntimeException{
    private final HttpStatus httpStatus = ErrorCode.DORMITORY_NOT_FOUND.getHttpStatus();
    private final String message = ErrorCode.DORMITORY_NOT_FOUND.getMessage();

}
