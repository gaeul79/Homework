package com.sparta.homework_login.dto.response;

import com.sparta.homework_login.enums.ErrorCode;
import com.sparta.homework_login.exception.BusinessException;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 에러에 대한 정보를 제공하는 DTO 클래스
 *
 * @since 2025-01-17
 */
@Data
public class ErrorResponseDto {
    @Schema(example = "2025-01-18 19:53:48", description = "에러 발생 시간 (yyyy-MM-dd HH:mm:ss)")
    private String date;

    @Schema(example = "400", description = "HTTP 상태 코드")
    private int state;

    @Schema(example = "잘못된 요청입니다.", description = "에러 메시지")
    private String message;

    @Schema(example = "http://localhost:8080/api/..", description = "요청한 URL")
    private String url;

    public ErrorResponseDto(BusinessException ex, String requestUrl) {
        date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        state = ex.getErrorCode().getHttpStatus().value();
        message = ex.getMessage();
        url = requestUrl;
    }

    public ErrorResponseDto(ErrorCode errorCode, String requestUrl) {
        date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        state = errorCode.getHttpStatus().value();
        message = errorCode.getMessage();
        url = requestUrl;
    }

    public ErrorResponseDto(ErrorCode errorCode, String requestUrl, String detailMsg) {
        date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        state = errorCode.getHttpStatus().value();
        message = errorCode.getMessage() + ": " + detailMsg;
        url = requestUrl;
    }
}
