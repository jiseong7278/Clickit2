package com.project.clickit.exceptions.login;

import com.project.clickit.exceptions.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class MailSendFailedException extends RuntimeException{
    private final HttpStatus httpStatus = ErrorCode.MAIL_SEND_FAILED.getHttpStatus();
    private final String message = ErrorCode.MAIL_SEND_FAILED.getMessage();
}
