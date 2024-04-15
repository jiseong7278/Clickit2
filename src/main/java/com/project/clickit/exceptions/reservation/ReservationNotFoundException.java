package com.project.clickit.exceptions.reservation;

import com.project.clickit.exceptions.ErrorCode;
import org.springframework.http.HttpStatus;

public class ReservationNotFoundException extends RuntimeException{
    private final HttpStatus httpStatus = ErrorCode.RESERVATION_NOT_FOUND.getHttpStatus();
    private final String message = ErrorCode.RESERVATION_NOT_FOUND.getMessage();
}
