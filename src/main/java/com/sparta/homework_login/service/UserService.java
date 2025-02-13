package com.sparta.homework_login.service;

import com.sparta.homework_login.common.UserValidationCheck;
import com.sparta.homework_login.dto.request.PasswordCheckRequestDto;
import com.sparta.homework_login.dto.request.SignUpRequestDto;
import com.sparta.homework_login.dto.request.UpdateUserRequestDto;
import com.sparta.homework_login.dto.response.SignUpResponseDto;
import com.sparta.homework_login.dto.response.UpdateUserResponseDto;
import com.sparta.homework_login.entity.User;
import com.sparta.homework_login.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final PasswordEncoder passwordEncoder;
    private final UserValidationCheck userValidationCheck;

    /**
     * 회원가입을 처리합니다.
     *
     * @param requestDto 회원가입 요청 정보
     * @return 회원가입 결과 (SignUpRequestDto)
     * @since 2025-01-17
     */
    public SignUpResponseDto signUp(@Valid SignUpRequestDto requestDto) {
        userValidationCheck.duplicationUser(requestDto.getUsername());
        User user = createUser(requestDto);
        userRepository.save(user);
        return SignUpResponseDto.create(user);
    }

    /**
     * 회원가입 요청 DTO를 기반으로 새로운 User 엔티티를 생성합니다.
     *
     * @param requestDto 회원가입 요청 정보 (SignUpRequestDto)
     * @return 생성된 User 엔티티 객체
     * @since 2025-01-17
     */
    private User createUser(SignUpRequestDto requestDto) {
        String password = passwordEncoder.encode(requestDto.getPassword());
        return requestDto.convertDtoToEntity(password);
    }

    /**
     * 회원수정 API
     *
     * @param id 로그인한 유저 ID
     * @param requestDto 수정할 유저 정보
     * @since 2025-02-13
     */
    @Transactional
    public UpdateUserResponseDto updateUser(Long id, UpdateUserRequestDto requestDto) {
        userValidationCheck.comparePassword(id, requestDto.getOriPassword());
        String newPassword = passwordEncoder.encode(requestDto.getNewPassword());
        User user = userValidationCheck.findUser(id);
        user.update(requestDto.getNickname(), newPassword);
        return UpdateUserResponseDto.create(user);
    }

    /**
     * 회원탈퇴 API
     *
     * @param id 로그인한 유저 ID
     * @param requestDto 확인용 비밀번호 (JSON 형태)
     * @since 2025-02-13
     */
    public void deleteUser(Long id, PasswordCheckRequestDto requestDto) {
        userValidationCheck.comparePassword(id, requestDto.getPassword());
        userRepository.deleteById(id);
    }
}
