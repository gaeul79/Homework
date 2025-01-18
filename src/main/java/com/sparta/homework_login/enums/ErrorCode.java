package com.sparta.homework_login.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * API 응답 시 사용되는 상태 코드와 메시지를 정의하는 enum
 *
 * @see <a href="https://ko.wikipedia.org/wiki/HTTP_%EC%83%81%ED%83%9C_%EC%BD%94%EB%93%9C">HTTP 상태 코드</a>
 * @since 2025-01-17
 */
@Getter
@AllArgsConstructor
public enum ErrorCode {
    // 400
    BAD_INPUT(HttpStatus.BAD_REQUEST, "잘못된 값 입력"),
    TOKEN_UNSIGNED(HttpStatus.BAD_REQUEST, "유효하지 않는 JWT 서명 입니다."),
    TOKEN_INVALID(HttpStatus.BAD_REQUEST, "잘못된 JWT 토큰 입니다."),
    TOKEN_FAIL_ENCODING(HttpStatus.BAD_REQUEST, "잘못된 인코딩을 사용하였습니다."),
    JSON_INVALID(HttpStatus.BAD_REQUEST, "잘못된 JSON형식 전송"),

    // 401
    TOKEN_TIMEOUT(HttpStatus.UNAUTHORIZED, "만료된 JWT token 입니다."),
    TOKEN_UNSUPPORTED(HttpStatus.UNAUTHORIZED, "지원되지 않는 JWT 토큰 입니다."),
    USER_PASSWORD_NOT_MATCH(HttpStatus.UNAUTHORIZED, "비밀번호가 틀렸습니다."),

    // 403
    INVALID_PERMISSION(HttpStatus.FORBIDDEN, "권한이 없습니다"),

    // 404
    TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, "JWT 토큰이 없습니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "유저를 찾을 수 없습니다."),
    USER_ROLE_NOT_FOUND(HttpStatus.NOT_FOUND, "유효하지 않은 유저 권한"),

    // 409
    USER_DUPLICATED(HttpStatus.CONFLICT, "이름이 중복됩니다."),

    // 500
    UNKNOWN_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "알 수 없는 오류");

    private final HttpStatus httpStatus;
    private final String message;
}