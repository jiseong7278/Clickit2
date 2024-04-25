package com.project.clickit.exceptions.common;

import com.project.clickit.exceptions.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ObjectNotFoundException extends RuntimeException{
    private ErrorCode errorCode;
}
