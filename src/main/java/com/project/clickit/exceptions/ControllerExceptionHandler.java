package com.project.clickit.exceptions;

import com.project.clickit.exceptions.login.ConcurrentlySignUpException;
import com.project.clickit.exceptions.login.DuplicatedIdException;
import com.project.clickit.exceptions.login.InvalidIdException;
import com.project.clickit.exceptions.login.InvalidPasswordException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerExceptionHandler {

    /**
     * 동시에 회원가입을 시도할 경우 발생하는 예외
     * @return 409 Conflict
     */
    @ExceptionHandler(ConcurrentlySignUpException.class)
    public ResponseEntity handleConcurrentlySignUpException(){
        return ResponseEntity.status(ErrorCode.CONCURRENTLY_SIGNUP.getHttpStatus()).body(ErrorCode.CONCURRENTLY_SIGNUP.getMessage());
    }

    /**
     * 이미 가입된 아이디일 경우 발생하는 예외
     * @return 400 Bad Request
     */
    @ExceptionHandler(DuplicatedIdException.class)
    public ResponseEntity handleDuplicatedIdException(){
        return ResponseEntity.status(ErrorCode.DUPLICATED_ID.getHttpStatus()).body(ErrorCode.DUPLICATED_ID.getMessage());
    }

    /**
     * 존재하지 않는 아이디일 경우 발생하는 예외
     * @return 400 Bad Request
     */
    @ExceptionHandler(InvalidIdException.class)
    public ResponseEntity handleInvalidIdException(){
        return ResponseEntity.status(ErrorCode.INVALID_ID.getHttpStatus()).body(ErrorCode.INVALID_ID.getMessage());
    }

    /**
     * 비밀번호가 일치하지 않을 경우 발생하는 예외
     * @return 400 Bad Request
     */
    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity handleInvalidPasswordException(){
        return ResponseEntity.status(ErrorCode.INVALID_PASSWORD.getHttpStatus()).body(ErrorCode.INVALID_PASSWORD.getMessage());
    }
}
