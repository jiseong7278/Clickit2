package com.project.clickit.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    CONCURRENTLY_SIGNUP(HttpStatus.CONFLICT, "잠시만 기다려 주세요."),
    DUPLICATED_ID(HttpStatus.BAD_REQUEST, "이미 가입된 아이디입니다."),
    INVALID_ID(HttpStatus.BAD_REQUEST, "아이디가 존재하지 않습니다."),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),
    MEMBER_NOT_FOUND(HttpStatus.BAD_REQUEST, "회원이 존재하지 않습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
