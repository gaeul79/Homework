package com.sparta.homework_login.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sparta.homework_login.dto.response.ErrorResponseDto;
import com.sparta.homework_login.enums.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 예외 처리 클래스.
 *
 * @since 2024-10-21
 */
@Slf4j(topic = "exception:")
@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * 입력 관련 예외 처리
     *
     * @since 2024-10-22
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> BaseException(MethodArgumentNotValidException ex, HttpServletRequest req) {
        // 에러 메시지 추출
        String errorMsg = ex.getBindingResult().
                getAllErrors()
                .get(0)
                .getDefaultMessage();

        return validException(req, errorMsg);
    }

    /**
     * ResponseException 예외 처리
     *
     * @since 2024-10-22
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponseDto> BaseException(BusinessException ex, HttpServletRequest req) {
        return baseException(req, ex);
    }

    /**
     * JSON 관련 예외 처리
     *
     * @since 2024-10-31
     */
    @ExceptionHandler(JsonProcessingException.class)
    public ResponseEntity<ErrorResponseDto> BaseException(JsonProcessingException ex, HttpServletRequest req) {
        printError(ex);
        return baseException(req, ErrorCode.JSON_INVALID);
    }

    /**
     * 인코딩 관련 예외 처리
     *
     * @since 2024-10-29
     */
    @ExceptionHandler(UnsupportedEncodingException.class)
    public ResponseEntity<ErrorResponseDto> BaseException(UnsupportedEncodingException ex, HttpServletRequest req) {
        printError(ex);
        return baseException(req, ErrorCode.TOKEN_FAIL_ENCODING);
    }

    /**
     * 그 외 기타 예외처리
     *
     * @since 2024-10-22
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> BaseException(Exception ex, HttpServletRequest req) {
        printError(ex);
        return baseException(req, ErrorCode.UNKNOWN_ERROR);
    }

    /**
     * 기본적인 예외 처리를 위한 메서드입니다.
     *
     * @param req HTTP 요청 객체
     * @param ex  발생한 예외 객체
     * @return HTTP 응답 객체
     * @since 2024-10-24
     */
    private ResponseEntity<ErrorResponseDto> baseException(HttpServletRequest req, BusinessException ex) {
        String url = req.getRequestURL().toString();
        return ResponseEntity
                .status(ex.getErrorCode().getHttpStatus())
                .body(new ErrorResponseDto(ex, url));
    }

    /**
     * 기본적인 예외 처리를 위한 메서드입니다.
     *
     * @param req          HTTP 요청 객체
     * @param errorCode 응답 코드
     * @return ResponseEntity 객체
     * @since 2024-10-24
     */
    private ResponseEntity<ErrorResponseDto> baseException(HttpServletRequest req, ErrorCode errorCode) {
        String url = req.getRequestURL().toString();
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(new ErrorResponseDto(errorCode, url));
    }

    /**
     * 유효성 검사 실패 시 발생하는 예외 처리를 위한 메서드입니다.
     *
     * @param req      HTTP 요청 객체
     * @param errorMsg 에러 메시지
     * @return ResponseEntity 객체
     * @since 2024-10-24
     */
    private ResponseEntity<ErrorResponseDto> validException(HttpServletRequest req, String errorMsg) {
        String url = req.getRequestURL().toString();
        return ResponseEntity
                .status(ErrorCode.BAD_INPUT.getHttpStatus())
                .body(new ErrorResponseDto(ErrorCode.BAD_INPUT, url, errorMsg));
    }

    /**
     * 예외 객체의 스택 트레이스를 배열로 가져옵니다.
     *
     * @param ex 발생한 예외 객체
     * @since 2024-11-05
     */
    public void printError(Exception ex) {
        StackTraceElement[] stackTraceElements = ex.getStackTrace();
        log.error(ex.getMessage(), stackTraceElements[0].toString());
    }
}