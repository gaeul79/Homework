package com.sparta.homework_login.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.homework_login.dto.response.ErrorResponseDto;
import com.sparta.homework_login.enums.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JsonUtil {
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 오류 응답을 JSON 형식으로 작성하여 클라이언트에게 반환하는 메서드입니다.
     *
     * @param req       HTTP 요청 객체 (로그인 시도 요청)
     * @param res       HTTP 응답 객체 (오류 응답 반환)
     * @param errorCode 응답에 포함될 오류 코드
     * @throws IOException 응답 본문을 작성하는 과정에서 I/O 오류 발생 가능
     * @since 2025-01-17
     */
    public void writeBody(HttpServletRequest req, HttpServletResponse res, ErrorCode errorCode) throws IOException {
        String url = req.getRequestURL().toString();
        ErrorResponseDto responseDto = new ErrorResponseDto(errorCode, url);
        String body = objectMapper.writeValueAsString(responseDto);

        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");
        res.setStatus(errorCode.getHttpStatus().value());
        res.getWriter().write(body);
    }
}
