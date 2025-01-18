package com.sparta.homework_login.controller;

import com.sparta.homework_login.dto.request.SignUpRequestDto;
import com.sparta.homework_login.dto.response.ErrorResponseDto;
import com.sparta.homework_login.dto.response.SignUpResponseDto;
import com.sparta.homework_login.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "회원 관리 API", description = "회원가입 및 사용자 관리 API")
public class UserController {

    private final UserService userService;

    /**
     * 회원가입 API
     *
     * @param requestDto 회원가입 정보 (JSON 형태)
     * @return 회원가입 처리 결과
     * @since 2025-01-17
     */
    @Operation(summary = "회원가입", description = "회원 정보를 입력받아 회원가입을 처리합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "회원가입 성공"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청 데이터",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "이미 존재하는 사용자입니다.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    })
    @PostMapping("/auth/users")
    public ResponseEntity<SignUpResponseDto> signUp(
            @RequestBody @Valid SignUpRequestDto requestDto
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userService.signUp(requestDto));
    }
}
