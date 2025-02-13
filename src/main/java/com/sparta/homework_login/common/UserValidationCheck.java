package com.sparta.homework_login.common;

import com.sparta.homework_login.entity.User;
import com.sparta.homework_login.enums.ErrorCode;
import com.sparta.homework_login.exception.BusinessException;
import com.sparta.homework_login.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserValidationCheck {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public void duplicationUser(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        if(user.isPresent()) {
            throw new BusinessException(ErrorCode.USER_DUPLICATED);
        }
    }

    public void comparePassword(Long id, String password) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new BusinessException(ErrorCode.USER_NOT_FOUND)
        );

        if(!passwordEncoder.matches(password, user.getPassword())) {
            throw new BusinessException(ErrorCode.USER_PASSWORD_NOT_MATCH);
        }
    }
}
