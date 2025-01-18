package com.sparta.homework_login.service;

import com.sparta.homework_login.common.UserValidationCheck;
import com.sparta.homework_login.dto.request.SignUpRequestDto;
import com.sparta.homework_login.dto.response.SignUpResponseDto;
import com.sparta.homework_login.enums.ErrorCode;
import com.sparta.homework_login.exception.BusinessException;
import com.sparta.homework_login.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Rollback
@Transactional
@SpringBootTest
public class UserServiceTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserValidationCheck userValidationCheck;

    UserService userService;

    private SignUpRequestDto createSignUpRequestDto(String username, String password, String nickname) {
        return SignUpRequestDto.builder()
                .username(username)
                .password(password)
                .nickname(nickname)
                .build();
    }

    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository, passwordEncoder, userValidationCheck);
    }

    @Test
    @DisplayName("회원 가입 성공")
    void signUp_Success() {
        // given
        SignUpRequestDto requestDto = createSignUpRequestDto(
                "Hong",
                "1q2w3e4r#",
                "동에 번쩍"
        );

        // when
        SignUpResponseDto responseDto = userService.signUp(requestDto);

        // then
        assertEquals(responseDto.getUserName(), requestDto.getUsername());
        assertEquals(responseDto.getNickname(), requestDto.getNickname());
    }

    @Test
    @DisplayName("회원 가입 실패 - 중복된 이름")
    void signUp_failure_duplicateUsername() {
        // given
        SignUpRequestDto requestDto1 = createSignUpRequestDto(
                "Hong",
                "1q2w3e4r#",
                "동에 번쩍"
        );

        SignUpRequestDto requestDto2 = createSignUpRequestDto(
                "Hong",
                "1q2w3e4r#",
                "서에 번쩍"
        );

        // when
        userService.signUp(requestDto1);
        Exception exception = assertThrows(BusinessException.class, () -> {
            userService.signUp(requestDto2);
        });

        // then
        assertEquals(
                ErrorCode.USER_DUPLICATED.getMessage(),
                exception.getMessage()
        );
    }
}
