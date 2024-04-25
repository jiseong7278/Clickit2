package com.project.clickit.exceptions.member;

import com.project.clickit.exceptions.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class MemberNotFoundException extends RuntimeException{
    private final HttpStatus httpStatus = ErrorCode.MEMBER_NOT_FOUND.getHttpStatus();
    private final String message = ErrorCode.MEMBER_NOT_FOUND.getMessage();
}
