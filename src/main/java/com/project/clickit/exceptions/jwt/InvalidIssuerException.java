package com.project.clickit.exceptions.jwt;

import com.project.clickit.exceptions.ErrorCode;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class InvalidIssuerException extends RuntimeException{
    private final ErrorCode errorCode;
}
