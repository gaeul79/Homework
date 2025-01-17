package com.sparta.homework_login.service;

import com.sparta.homework_login.dto.request.SignUpRequestDto;
import com.sparta.homework_login.dto.response.SignUpResponseDto;
import com.sparta.homework_login.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 사용자 관리 서비스 클래스
 * UserRepository 이용하여 사용자를 관리합니다.
 *
 * @since 2025-01-17
 */
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    /**
     * 회원가입을 처리합니다.
     *
     * @param requestDto 회원가입 요청 정보
     * @return 회원가입 결과 (SignUpRequestDto)
     * @since 2024-10-03
     */
    public SignUpResponseDto signUp(@Valid SignUpRequestDto requestDto) {
        return null;
    }
}
