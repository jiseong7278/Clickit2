package com.project.clickit.exceptions.jwt;

import com.project.clickit.exceptions.ErrorCode;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UnexpectedTokenException extends RuntimeException{
    private final ErrorCode errorCode;
}
