package com.sparta.homework_login.service;

import com.sparta.homework_login.common.UserValidationCheck;
import com.sparta.homework_login.dto.request.PasswordCheckRequestDto;
import com.sparta.homework_login.dto.request.SignUpRequestDto;
import com.sparta.homework_login.dto.request.UpdateUserRequestDto;
import com.sparta.homework_login.dto.response.SignUpResponseDto;
import com.sparta.homework_login.dto.response.UpdateUserResponseDto;
import com.sparta.homework_login.entity.User;
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

    private UpdateUserRequestDto createUpdateUserRequestDto(String oriPassword, String newPassword, String nickname) {
        return UpdateUserRequestDto.builder()
                .nickname(nickname)
                .oriPassword(oriPassword)
                .newPassword(newPassword)
                .build();
    }

    private User findUser(String username) {
        return userRepository.findByUsername(username).orElseThrow(
                () -> new BusinessException(ErrorCode.USER_NOT_FOUND)
        );
    }

    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository, passwordEncoder, userValidationCheck);
    }

    @Test
    @DisplayName("회원 가입 성공")
    void signUp_success() {
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

    @Test
    @DisplayName("회원 수정 성공")
    void updateUser_success() {
        // given
        SignUpRequestDto createDto = createSignUpRequestDto(
                "Hong",
                "1q2w3e4r#",
                "동에 번쩍"
        );

        UpdateUserRequestDto updateDto = createUpdateUserRequestDto(
                createDto.getPassword(),
                "Admin123!",
                "서에 번쩍"
        );

        // when
        userService.signUp(createDto);
        User user = findUser(createDto.getUsername());
        UpdateUserResponseDto responseDto = userService.updateUser(user.getId(), updateDto);

        // then
        assertEquals(responseDto.getUserName(), createDto.getUsername());
        assertEquals(responseDto.getNickname(), updateDto.getNickname());
    }

    @Test
    @DisplayName("회원 수정 실패 - 확인용 비밀번호 불일치")
    void updateUser_failure_notMatchPassword() {
        // given
        SignUpRequestDto createDto = createSignUpRequestDto(
                "Hong",
                "1q2w3e4r#",
                "동에 번쩍"
        );

        UpdateUserRequestDto updateDto = createUpdateUserRequestDto(
                "asdf1234!",
                "Admin123!",
                "서에 번쩍"
        );

        // when
        userService.signUp(createDto);
        User user = findUser(createDto.getUsername());
        Exception exception = assertThrows(BusinessException.class, () -> {
            userService.updateUser(user.getId(), updateDto);
        });

        // then
        assertEquals(
                ErrorCode.USER_PASSWORD_NOT_MATCH.getMessage(),
                exception.getMessage()
        );
    }

    @Test
    @DisplayName("회원 탈퇴 성공")
    void deleteUser_success() {
        // given
        SignUpRequestDto createDto = createSignUpRequestDto(
                "Hong",
                "1q2w3e4r#",
                "동에 번쩍"
        );

        PasswordCheckRequestDto deleteDto = new PasswordCheckRequestDto(
                "1q2w3e4r#"
        );

        // when
        userService.signUp(createDto);
        User user = findUser(createDto.getUsername());
        userService.deleteUser(user.getId(), deleteDto);
        Exception exception = assertThrows(BusinessException.class, () -> {
            findUser(createDto.getUsername());
        });

        // then
        assertEquals(
                ErrorCode.USER_NOT_FOUND.getMessage(),
                exception.getMessage()
        );
    }

    @Test
    @DisplayName("회원 탈퇴 실패 - 확인용 비밀번호 불일치")
    void deleteUser_failure_notMatchPassword() {
        // given
        SignUpRequestDto createDto = createSignUpRequestDto(
                "Hong",
                "1q2w3e4r#",
                "동에 번쩍"
        );

        PasswordCheckRequestDto deleteDto = new PasswordCheckRequestDto(
                "asdf1234@"
        );

        // when
        userService.signUp(createDto);
        User user = findUser(createDto.getUsername());
        Exception exception = assertThrows(BusinessException.class, () -> {
            userService.deleteUser(user.getId(), deleteDto);
        });

        // then
        assertEquals(
                ErrorCode.USER_PASSWORD_NOT_MATCH.getMessage(),
                exception.getMessage()
        );
    }
}
