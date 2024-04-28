package com.project.clickit.exceptions.common;

import com.project.clickit.exceptions.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NoPermissionException extends RuntimeException{
    private final ErrorCode errorCode;
}
