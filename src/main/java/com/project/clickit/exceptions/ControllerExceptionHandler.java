package com.project.clickit.exceptions;

import com.project.clickit.exceptions.jwt.*;
import com.project.clickit.exceptions.login.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerExceptionHandler {

    // jwt

    /**
     * 토큰이 존재하지 않을 경우 발생하는 예외
     * @return 400 Bad Request
     */
    @ExceptionHandler(TokenNotFoundException.class)
    public ResponseEntity handleTokenNotFoundException(){
        return ResponseEntity.status(ErrorCode.TOKEN_NOT_FOUND.getHttpStatus()).body(ErrorCode.TOKEN_NOT_FOUND.getMessage());
    }

    /**
     * 유효하지 않은 발급자일 경우 발생하는 예외
     * @return 400 Bad Request
     */
    @ExceptionHandler(InvalidIssuerException.class)
    public ResponseEntity handleInvalidIssuerException(){
        return ResponseEntity.status(ErrorCode.INVALID_ISSUER.getHttpStatus()).body(ErrorCode.INVALID_ISSUER.getMessage());
    }

    /**
     * 유효하지 않은 시그니처일 경우 발생하는 예외
     * @return 400 Bad Request
     */
    @ExceptionHandler(InvalidSignatureException.class)
    public ResponseEntity handleInvalidSignatureException(){
        return ResponseEntity.status(ErrorCode.INVALID_SIGNATURE_TOKEN.getHttpStatus()).body(ErrorCode.INVALID_SIGNATURE_TOKEN.getMessage());
    }

    /**
     * 토큰이 만료되었을 경우 발생하는 예외
     * @return 400 Bad Request
     */
    @ExceptionHandler(ExpiredTokenException.class)
    public ResponseEntity handleExpiredTokenException(){
        return ResponseEntity.status(ErrorCode.EXPIRED_TOKEN.getHttpStatus()).body(ErrorCode.EXPIRED_TOKEN.getMessage());
    }

    /**
     * 지원하지 않는 토큰일 경우 발생하는 예외
     * @return 400 Bad Request
     */
    @ExceptionHandler(UnsupportedTokenException.class)
    public ResponseEntity handleUnsupportedTokenException(){
        return ResponseEntity.status(ErrorCode.UNSUPPORTED_TOKEN.getHttpStatus()).body(ErrorCode.UNSUPPORTED_TOKEN.getMessage());
    }

    /**
     * 토큰이 잘못되었을 경우 발생하는 예외
     * @return 400 Bad Request
     */
    @ExceptionHandler(IllegalTokenException.class)
    public ResponseEntity handleIllegalTokenException(){
        return ResponseEntity.status(ErrorCode.ILLEGAL_TOKEN.getHttpStatus()).body(ErrorCode.ILLEGAL_TOKEN.getMessage());
    }

    /**
     * 예상치 못한 토큰 예외 발생 시
     * @return 400 Bad Request
     */
    @ExceptionHandler(UnexpectedTokenException.class)
    public ResponseEntity handleUnexpectedTokenException(){
        return ResponseEntity.status(ErrorCode.UNEXPECTED_TOKEN_ERROR.getHttpStatus()).body(ErrorCode.UNEXPECTED_TOKEN_ERROR.getMessage());
    }

    // login
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

    /**
     * 회원이 존재하지 않을 경우 발생하는 예외
     * @return 400 Bad Request
     */
    @ExceptionHandler(MemberNotFoundException.class)
    public ResponseEntity handleMemberNotFoundException(){
        return ResponseEntity.status(ErrorCode.MEMBER_NOT_FOUND.getHttpStatus()).body(ErrorCode.MEMBER_NOT_FOUND.getMessage());
    }
}
