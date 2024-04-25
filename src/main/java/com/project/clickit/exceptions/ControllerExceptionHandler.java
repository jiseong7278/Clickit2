package com.project.clickit.exceptions;

import com.project.clickit.exceptions.common.DuplicatedIdException;
import com.project.clickit.exceptions.common.InvalidIdException;
import com.project.clickit.exceptions.common.ObjectNotFoundException;
import com.project.clickit.exceptions.image.*;
import com.project.clickit.exceptions.jwt.*;
import com.project.clickit.exceptions.login.*;
import com.project.clickit.exceptions.reservation.DuplicatedReservationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ControllerExceptionHandler extends RuntimeException{

    // common
    /**
     * 해당 객체가 존재하지 않을 경우 발생하는 예외
     * @return 400 Bad Request
     */
    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<String> handleObjectNotFoundException(ObjectNotFoundException e){
        return ResponseEntity.status(e.getErrorCode().getHttpStatus()).body(e.getErrorCode().getMessage());
    }

    /**
     * 해당 아이디가 이미 존재하는 경우 발생하는 예외
     * @return 400 Bad Request
     */
    @ExceptionHandler(DuplicatedIdException.class)
    public ResponseEntity<String> handleDuplicatedIdException(DuplicatedIdException e){
        return ResponseEntity.status(e.getErrorCode().getHttpStatus()).body(e.getErrorCode().getMessage());
    }

    /**
     * 존재하지 않는 아이디일 경우 발생하는 예외
     * @return 400 Bad Request
     */
    @ExceptionHandler(InvalidIdException.class)
    public ResponseEntity<String> handleInvalidIdException(InvalidIdException e){
        return ResponseEntity.status(e.getErrorCode().getHttpStatus()).body(e.getErrorCode().getMessage());
    }


    // jwt
    /**
     * 토큰 예외 발생 시
     * @return 400 Bad Request
     */
    @ExceptionHandler(JWTException.class)
    public ResponseEntity<String> handleJWTException(JWTException e){
        return ResponseEntity.status(e.getErrorCode().getHttpStatus()).body(e.getErrorCode().getMessage());
    }


    // login
    /**
     * 동시에 회원가입을 시도할 경우 발생하는 예외
     * @return 409 Conflict
     */
    @ExceptionHandler(ConcurrentlySignUpException.class)
    public ResponseEntity<String> handleConcurrentlySignUpException(ConcurrentlySignUpException e){
        return ResponseEntity.status(e.getErrorCode().getHttpStatus()).body(e.getErrorCode().getMessage());
    }

    /**
     * 로그인에 실패했을 경우 발생하는 예외
     * @return 400 Bad Request
     */
    @ExceptionHandler(SignInFailedException.class)
    public ResponseEntity<String> handleSignInFailedException(SignInFailedException e){
        return ResponseEntity.status(e.getErrorCode().getHttpStatus()).body(e.getErrorCode().getMessage());
    }

    /**
     * 메일 전송에 실패했을 경우 발생하는 예외
     * @return 400 Bad Request
     */
    @ExceptionHandler(MailSendFailedException.class)
    public ResponseEntity<String> handleMailSendFailedException(MailSendFailedException e){
        return ResponseEntity.status(e.getErrorCode().getHttpStatus()).body(e.getErrorCode().getMessage());
    }


    // reservation
    /**
     * 이미 예약된 좌석일 경우 발생하는 예외
     * @return 400 Bad Request
     */
    @ExceptionHandler(DuplicatedReservationException.class)
    public ResponseEntity<String> handleDuplicatedReservationException(DuplicatedReservationException e){
        return ResponseEntity.status(e.getErrorCode().getHttpStatus()).body(e.getErrorCode().getMessage());
    }


    // image
    /**
     * S3 이미지 업로드 중 오류가 발생했을 경우 발생하는 예외
     * @return 400 Bad Request
     */
    @ExceptionHandler(S3ImageException.class)
    public ResponseEntity<String> handleS3ImageException(S3ImageException e){
        return ResponseEntity.status(e.getErrorCode().getHttpStatus()).body(e.getErrorCode().getMessage());
    }


    // access denied
    /**
     * 접근 권한이 없을 경우 발생하는 예외
     * @return 403 Forbidden
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> handleAccessDeniedException(){
        return ResponseEntity.status(ErrorCode.ACCESS_DENIED.getHttpStatus()).body(ErrorCode.ACCESS_DENIED.getMessage());
    }
}
