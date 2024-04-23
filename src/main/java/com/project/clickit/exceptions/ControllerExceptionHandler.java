package com.project.clickit.exceptions;

import com.project.clickit.exceptions.common.DuplicatedIdException;
import com.project.clickit.exceptions.common.InvalidIdException;
import com.project.clickit.exceptions.common.ObjectNotFoundException;
import com.project.clickit.exceptions.dormitory.DormitoryNotFoundException;
import com.project.clickit.exceptions.facility.FacilityNotFoundException;
import com.project.clickit.exceptions.jwt.*;
import com.project.clickit.exceptions.login.*;
import com.project.clickit.exceptions.notice.NoticeNotFoundException;
import com.project.clickit.exceptions.reservation.DuplicatedReservationException;
import com.project.clickit.exceptions.reservation.ReservationNotFoundException;
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
    public ResponseEntity<String> handleObjectNotFoundException(){
        return ResponseEntity.status(ErrorCode.OBJECT_NOT_FOUND.getHttpStatus()).body(ErrorCode.OBJECT_NOT_FOUND.getMessage());
    }

    // jwt

    /**
     * 토큰이 존재하지 않을 경우 발생하는 예외
     * @return 400 Bad Request
     */
    @ExceptionHandler(TokenNotFoundException.class)
    public ResponseEntity<String> handleTokenNotFoundException(){
        return ResponseEntity.status(ErrorCode.TOKEN_NOT_FOUND.getHttpStatus()).body(ErrorCode.TOKEN_NOT_FOUND.getMessage());
    }

    /**
     * 유효하지 않은 발급자일 경우 발생하는 예외
     * @return 400 Bad Request
     */
    @ExceptionHandler(InvalidIssuerException.class)
    public ResponseEntity<String> handleInvalidIssuerException(){
        return ResponseEntity.status(ErrorCode.INVALID_ISSUER.getHttpStatus()).body(ErrorCode.INVALID_ISSUER.getMessage());
    }

    /**
     * 유효하지 않은 시그니처일 경우 발생하는 예외
     * @return 400 Bad Request
     */
    @ExceptionHandler(InvalidSignatureException.class)
    public ResponseEntity<String> handleInvalidSignatureException(){
        return ResponseEntity.status(ErrorCode.INVALID_SIGNATURE_TOKEN.getHttpStatus()).body(ErrorCode.INVALID_SIGNATURE_TOKEN.getMessage());
    }

    /**
     * 토큰이 만료되었을 경우 발생하는 예외
     * @return 400 Bad Request
     */
    @ExceptionHandler(ExpiredTokenException.class)
    public ResponseEntity<String> handleExpiredTokenException(){
        return ResponseEntity.status(ErrorCode.EXPIRED_TOKEN.getHttpStatus()).body(ErrorCode.EXPIRED_TOKEN.getMessage());
    }

    /**
     * 지원하지 않는 토큰일 경우 발생하는 예외
     * @return 400 Bad Request
     */
    @ExceptionHandler(UnsupportedTokenException.class)
    public ResponseEntity<String> handleUnsupportedTokenException(){
        return ResponseEntity.status(ErrorCode.UNSUPPORTED_TOKEN.getHttpStatus()).body(ErrorCode.UNSUPPORTED_TOKEN.getMessage());
    }

    /**
     * 토큰이 잘못되었을 경우 발생하는 예외
     * @return 400 Bad Request
     */
    @ExceptionHandler(IllegalTokenException.class)
    public ResponseEntity<String> handleIllegalTokenException(){
        return ResponseEntity.status(ErrorCode.ILLEGAL_TOKEN.getHttpStatus()).body(ErrorCode.ILLEGAL_TOKEN.getMessage());
    }

    /**
     * 예상치 못한 토큰 예외 발생 시
     * @return 400 Bad Request
     */
    @ExceptionHandler(UnexpectedTokenException.class)
    public ResponseEntity<String> handleUnexpectedTokenException(){
        return ResponseEntity.status(ErrorCode.UNEXPECTED_TOKEN_ERROR.getHttpStatus()).body(ErrorCode.UNEXPECTED_TOKEN_ERROR.getMessage());
    }

    // login
    /**
     * 동시에 회원가입을 시도할 경우 발생하는 예외
     * @return 409 Conflict
     */
    @ExceptionHandler(ConcurrentlySignUpException.class)
    public ResponseEntity<String> handleConcurrentlySignUpException(){
        return ResponseEntity.status(ErrorCode.CONCURRENTLY_SIGNUP.getHttpStatus()).body(ErrorCode.CONCURRENTLY_SIGNUP.getMessage());
    }

    /**
     * 이미 가입된 아이디일 경우 발생하는 예외
     * @return 400 Bad Request
     */
    @ExceptionHandler(DuplicatedIdException.class)
    public ResponseEntity<String> handleDuplicatedIdException(){
        return ResponseEntity.status(ErrorCode.DUPLICATED_ID.getHttpStatus()).body(ErrorCode.DUPLICATED_ID.getMessage());
    }

    /**
     * 존재하지 않는 아이디일 경우 발생하는 예외
     * @return 400 Bad Request
     */
    @ExceptionHandler(InvalidIdException.class)
    public ResponseEntity<String> handleInvalidIdException(){
        return ResponseEntity.status(ErrorCode.INVALID_ID.getHttpStatus()).body(ErrorCode.INVALID_ID.getMessage());
    }

    /**
     * 비밀번호가 일치하지 않을 경우 발생하는 예외
     * @return 400 Bad Request
     */
    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<String> handleInvalidPasswordException(){
        return ResponseEntity.status(ErrorCode.INVALID_PASSWORD.getHttpStatus()).body(ErrorCode.INVALID_PASSWORD.getMessage());
    }

    /**
     * 회원이 존재하지 않을 경우 발생하는 예외
     * @return 400 Bad Request
     */
    @ExceptionHandler(MemberNotFoundException.class)
    public ResponseEntity<String> handleMemberNotFoundException(){
        return ResponseEntity.status(ErrorCode.MEMBER_NOT_FOUND.getHttpStatus()).body(ErrorCode.MEMBER_NOT_FOUND.getMessage());
    }


    // member


    // dormitory
    /**
     * 기숙사가 존재하지 않을 경우 발생하는 예외
     * @return 400 Bad Request
     */
    @ExceptionHandler(DormitoryNotFoundException.class)
    public ResponseEntity<String> handleDormitoryNotFoundException(){
        return ResponseEntity.status(ErrorCode.DORMITORY_NOT_FOUND.getHttpStatus()).body(ErrorCode.DORMITORY_NOT_FOUND.getMessage());
    }

    // facility
    /**
     * 시설이 존재하지 않을 경우 발생하는 예외
     * @return 400 Bad Request
     */
    @ExceptionHandler(FacilityNotFoundException.class)
    public ResponseEntity<String> handleFacilityNotFoundException(){
        return ResponseEntity.status(ErrorCode.FACILITY_NOT_FOUND.getHttpStatus()).body(ErrorCode.FACILITY_NOT_FOUND.getMessage());
    }

    // notice
    /**
     * 공지사항이 존재하지 않을 경우 발생하는 예외
     * @return 400 Bad Request
     */
    @ExceptionHandler(NoticeNotFoundException.class)
    public ResponseEntity<String> handleNoticeNotFoundException(){
        return ResponseEntity.status(ErrorCode.NOTICE_NOT_FOUND.getHttpStatus()).body(ErrorCode.NOTICE_NOT_FOUND.getMessage());
    }

    // reservation
    /**
     * 이미 예약된 좌석일 경우 발생하는 예외
     * @return 400 Bad Request
     */
    @ExceptionHandler(DuplicatedReservationException.class)
    public ResponseEntity<String> handleDuplicatedReservationException(){
        return ResponseEntity.status(ErrorCode.DUPLICATED_RESERVATION.getHttpStatus()).body(ErrorCode.DUPLICATED_RESERVATION.getMessage());
    }

    /**
     * 예약이 존재하지 않을 경우 발생하는 예외
     * @return 400 Bad Request
     */
    @ExceptionHandler(ReservationNotFoundException.class)
    public ResponseEntity<String> handleReservationNotFoundException(){
        return ResponseEntity.status(ErrorCode.RESERVATION_NOT_FOUND.getHttpStatus()).body(ErrorCode.RESERVATION_NOT_FOUND.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> handleAccessDeniedException(){
        return ResponseEntity.status(ErrorCode.ACCESS_DENIED.getHttpStatus()).body(ErrorCode.ACCESS_DENIED.getMessage());
    }
}
