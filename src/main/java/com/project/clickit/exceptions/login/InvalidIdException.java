package com.project.clickit.exceptions.login;

import com.project.clickit.exceptions.ErrorCode;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class InvalidIdException extends RuntimeException{
    private final ErrorCode errorCode;
}
