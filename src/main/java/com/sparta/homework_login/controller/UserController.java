package com.sparta.homework_login.controller;

import com.sparta.homework_login.dto.request.PasswordCheckRequestDto;
import com.sparta.homework_login.dto.request.SignUpRequestDto;
import com.sparta.homework_login.dto.request.UpdateUserRequestDto;
import com.sparta.homework_login.dto.response.ErrorResponseDto;
import com.sparta.homework_login.dto.response.SignUpResponseDto;
import com.sparta.homework_login.dto.response.UpdateUserResponseDto;
import com.sparta.homework_login.dto.security.UserDetailsImpl;
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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

    @Operation(summary = "회원수정", description = "수정할 정보와 기존 비밀번호를 입력받아 회원정보를 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "회원 정보 수정 성공"
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
                    responseCode = "401",
                    description = "확인용 비밀번호가 일치하지 않음",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "유저가 존재하지 않음",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    })
    @PutMapping("/users")
    public ResponseEntity<UpdateUserResponseDto> updateUser(
            @AuthenticationPrincipal UserDetailsImpl userDetail,
            @RequestBody @Valid UpdateUserRequestDto requestDto) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.updateUser(userDetail.getUsername(), requestDto));
    }

    /**
     * 회원탈퇴 API
     *
     * @param userDetail 로그인한 유저 정보
     * @param requestDto 확인용 비밀번호 (JSON 형태)
     * @since 2025-02-13
     */
    @Operation(summary = "회원탈퇴", description = "비밀번호를 입력받아 탈퇴합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "회원탈퇴 성공"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "확인용 비밀번호가 일치하지 않음",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "유저가 존재하지 않음",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    })
    @DeleteMapping("/users")
    public ResponseEntity<Void> deleteUser(
            @AuthenticationPrincipal UserDetailsImpl userDetail,
            @RequestBody @Valid PasswordCheckRequestDto requestDto
    ) {
        userService.deleteUser(userDetail, requestDto);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT).build();
    }
}