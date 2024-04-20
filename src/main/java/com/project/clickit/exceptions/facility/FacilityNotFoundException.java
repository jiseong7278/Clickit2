package com.project.clickit.exceptions.facility;

import com.project.clickit.exceptions.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class FacilityNotFoundException extends RuntimeException{
    private final HttpStatus httpStatus = ErrorCode.FACILITY_NOT_FOUND.getHttpStatus();
    private final String message = ErrorCode.FACILITY_NOT_FOUND.getMessage();
}
