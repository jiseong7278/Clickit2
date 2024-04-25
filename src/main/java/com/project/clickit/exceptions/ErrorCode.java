package com.project.clickit.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // access denied
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),

    // image
    IMAGE_NOT_EXISTS(HttpStatus.BAD_REQUEST, "이미지가 존재하지 않습니다."),
    EXTENSION_NOT_EXISTS(HttpStatus.BAD_REQUEST, "확장자가 존재하지 않습니다."),
    INVALID_IMAGE_FILE_EXTENSION(HttpStatus.BAD_REQUEST, "올바르지 않은 이미지 확장자입니다."),
    IO_EXCEPTION_ON_IMAGE_UPLOAD(HttpStatus.BAD_REQUEST, "이미지 업로드 중 오류가 발생했습니다."),
    PUT_IMAGE_EXCEPTION(HttpStatus.BAD_REQUEST, "이미지 업로드 중 오류가 발생했습니다."),
    IO_EXCEPTION_ON_IMAGE_DELETE(HttpStatus.BAD_REQUEST, "이미지 삭제 중 오류가 발생했습니다."),

    // common
    DUPLICATED_ID(HttpStatus.BAD_REQUEST, "이미 존재하는 아이디입니다."),
    INVALID_ID(HttpStatus.BAD_REQUEST, "아이디가 올바르지 않습니다."),
    OBJECT_NOT_FOUND(HttpStatus.BAD_REQUEST, "해당 객체가 존재하지 않습니다."),

    // jwt
    TOKEN_NOT_FOUND(HttpStatus.BAD_REQUEST, "토큰이 존재하지 않습니다."),
    INVALID_ISSUER(HttpStatus.BAD_REQUEST, "유효하지 않은 발급자입니다."),
    INVALID_SIGNATURE_TOKEN(HttpStatus.BAD_REQUEST, "유효하지 않은 서명입니다."),
    EXPIRED_TOKEN(HttpStatus.BAD_REQUEST, "만료된 토큰입니다."),
    UNSUPPORTED_TOKEN(HttpStatus.BAD_REQUEST, "지원하지 않는 토큰입니다."),
    ILLEGAL_TOKEN(HttpStatus.BAD_REQUEST, "잘못된 토큰입니다."),
    UNEXPECTED_TOKEN_ERROR(HttpStatus.BAD_REQUEST, "예상치 못한 토큰입니다."),

    // login
    CONCURRENTLY_SIGNUP(HttpStatus.CONFLICT, "잠시만 기다려 주세요."),
    SIGN_IN_FAILED(HttpStatus.BAD_REQUEST, "아이디 또는 비밀번호가 올바르지 않습니다."),
    MAIL_SEND_FAILED(HttpStatus.BAD_REQUEST, "메일 전송에 실패했습니다."),

    // member
    MEMBER_NOT_FOUND(HttpStatus.BAD_REQUEST, "회원이 존재하지 않습니다."),

    // dormitory
    DORMITORY_NOT_FOUND(HttpStatus.BAD_REQUEST, "기숙사가 존재하지 않습니다."),

    // facility
    FACILITY_NOT_FOUND(HttpStatus.BAD_REQUEST, "시설이 존재하지 않습니다."),

    // notice
    NOTICE_NOT_FOUND(HttpStatus.BAD_REQUEST, "공지사항이 존재하지 않습니다."),

    // reservation
    DUPLICATED_RESERVATION(HttpStatus.BAD_REQUEST, "이미 예약된 좌석입니다."),
    RESERVATION_NOT_FOUND(HttpStatus.BAD_REQUEST, "예약이 존재하지 않습니다."),

    // seat
    SEAT_NOT_FOUND(HttpStatus.BAD_REQUEST, "좌석이 존재하지 않습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
