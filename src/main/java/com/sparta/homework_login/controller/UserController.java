package com.sparta.homework_login.controller;

import com.sparta.homework_login.dto.request.SignUpRequestDto;
import com.sparta.homework_login.dto.response.SignUpResponseDto;
import com.sparta.homework_login.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 회원 관리 컨트롤러 클래스입니다.
 *
 * @since 2025-01-17
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    /**
     * 회원가입 API
     *
     * @param requestDto 회원가입 정보 (JSON 형태)
     * @return 회원가입 처리 결과
     * @since 2025-01-17
     */
    @PostMapping("/users")
    public ResponseEntity<SignUpResponseDto> signUp(
            @RequestBody @Valid SignUpRequestDto requestDto
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userService.signUp(requestDto));
    }
}
