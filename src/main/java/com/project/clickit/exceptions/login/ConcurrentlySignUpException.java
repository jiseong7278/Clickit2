package com.project.clickit.exceptions.login;

import com.project.clickit.exceptions.ErrorCode;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ConcurrentlySignUpException extends RuntimeException{
    private final ErrorCode errorCode;
}
