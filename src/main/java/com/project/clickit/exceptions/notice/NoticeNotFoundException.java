package com.project.clickit.exceptions.notice;

import com.project.clickit.exceptions.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class NoticeNotFoundException extends RuntimeException{
    private final HttpStatus httpStatus = ErrorCode.NOTICE_NOT_FOUND.getHttpStatus();
    private final String message = ErrorCode.NOTICE_NOT_FOUND.getMessage();
}
