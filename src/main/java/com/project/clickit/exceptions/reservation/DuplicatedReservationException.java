package com.project.clickit.exceptions.reservation;

import com.project.clickit.exceptions.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class DuplicatedReservationException extends RuntimeException{
    private final HttpStatus httpStatus = ErrorCode.DUPLICATED_RESERVATION.getHttpStatus();
    private final String message = ErrorCode.DUPLICATED_RESERVATION.getMessage();
}
