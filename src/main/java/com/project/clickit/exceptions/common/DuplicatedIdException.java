package com.project.clickit.exceptions.common;

import com.project.clickit.exceptions.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class DuplicatedIdException extends RuntimeException{
    private ErrorCode errorCode;
}
