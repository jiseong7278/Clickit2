package com.project.clickit.exceptions.seat;

import com.project.clickit.exceptions.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class SeatNotFoundException extends RuntimeException{
    private final HttpStatus httpStatus = ErrorCode.SEAT_NOT_FOUND.getHttpStatus();
    private final String message = ErrorCode.SEAT_NOT_FOUND.getMessage();
}
