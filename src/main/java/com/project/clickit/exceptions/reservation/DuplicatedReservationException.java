package com.project.clickit.exceptions.reservation;

import com.project.clickit.exceptions.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DuplicatedReservationException extends RuntimeException{
    private ErrorCode errorCode;
}
