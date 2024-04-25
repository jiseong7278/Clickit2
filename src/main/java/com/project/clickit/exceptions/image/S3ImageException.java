package com.project.clickit.exceptions.image;

import com.project.clickit.exceptions.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class S3ImageException extends RuntimeException{
    private ErrorCode errorCode;
}
