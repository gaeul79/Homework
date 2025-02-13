package com.sparta.homework_login.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.homework_login.dto.request.PasswordCheckRequestDto;
import com.sparta.homework_login.dto.request.SignInRequestDto;
import com.sparta.homework_login.dto.request.SignUpRequestDto;
import com.sparta.homework_login.dto.request.UpdateUserRequestDto;
import com.sparta.homework_login.entity.User;
import com.sparta.homework_login.mock.WithCustomMockUser;
import com.sparta.homework_login.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Rollback
@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS) // static 없이 @BeforeAll사용 가능
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void createUser() {
        userRepository.deleteAll();
        User user = User.builder()
                .username("Hong")
                .password(passwordEncoder.encode("1q2w3e4r#"))
                .nickname("동에 번쩍")
                .build();
        userRepository.save(user);
    }

    @Test
    @DisplayName("회원가입 성공")
    public void signUp_success() throws Exception {
        // given
        SignUpRequestDto requestDto = new SignUpRequestDto(
                "Hong",
                "1q2w3e4r#",
                "동에 번쩍"
        );

        // when
        ResultActions actions = mockMvc.perform(post("/api/auth/users")
                .content(objectMapper.writeValueAsString(requestDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        // then
        actions.andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("회원가입 실패 - 잘못된 값 입력")
    public void signUp_failure_badInput() throws Exception {
        // given
        SignUpRequestDto requestDto = new SignUpRequestDto(
                "Hong",
                "1q2w3e4r#",
                null
        );

        // when
        ResultActions actions = mockMvc.perform(post("/api/auth/users")
                .content(objectMapper.writeValueAsString(requestDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        // then
        actions.andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("회원가입 실패 - 중복된 회원")
    public void signUp_failure_duplication() throws Exception {
        // given
        SignUpRequestDto requestDto1 = new SignUpRequestDto(
                "Hong",
                "1q2w3e4r#",
                "동에 번쩍"
        );

        SignUpRequestDto requestDto2 = new SignUpRequestDto(
                "Hong",
                "1q2w3e4r#",
                "서에 번쩍"
        );

        // when
        ResultActions actions1 = mockMvc.perform(post("/api/auth/users")
                .content(objectMapper.writeValueAsString(requestDto1))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        ResultActions actions2 = mockMvc.perform(post("/api/auth/users")
                .content(objectMapper.writeValueAsString(requestDto2))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        // then
        actions1.andDo(print())
                .andExpect(status().isCreated());
        actions2.andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("로그인 성공")
    public void signIn_success() throws Exception {
        // given
        createUser();
        SignInRequestDto requestDto = new SignInRequestDto(
                "Hong",
                "1q2w3e4r#"
        );

        // when
        ResultActions actions = mockMvc.perform(post("/api/auth/login")
                .content(objectMapper.writeValueAsString(requestDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        // then
        actions.andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("로그인 실패 - 존재하지 않는 유저")
    public void signIn_failure_notMatchUsername() throws Exception {
        // given
        createUser();
        SignInRequestDto requestDto = new SignInRequestDto(
                "Kim",
                "1q2w3e4r#"
        );

        // when
        ResultActions actions = mockMvc.perform(post("/api/auth/login")
                .content(objectMapper.writeValueAsString(requestDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        // then
        actions.andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("로그인 실패 - 비밀번호 불일치")
    public void signIn_failure_notMatchPassword() throws Exception {
        // given
        createUser();
        SignInRequestDto requestDto = new SignInRequestDto(
                "Hong",
                "1q2w3e4r"
        );

        // when
        ResultActions actions = mockMvc.perform(post("/api/auth/login")
                .content(objectMapper.writeValueAsString(requestDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        // then
        actions.andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithCustomMockUser
    @DisplayName("회원 정보 수정 성공")
    public void updateUser_success() throws Exception {
        // given
        createUser();
        UpdateUserRequestDto requestDto = new UpdateUserRequestDto(
                "1q2w3e4r#",
                "Admin123!",
                "서에 번쩍"
        );

        // when
        ResultActions actions = mockMvc.perform(put("/api/users")
                .content(objectMapper.writeValueAsString(requestDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        // then
        actions.andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithCustomMockUser
    @DisplayName("회원 정보 수정 실패 - 비밀번호 불일치")
    public void updateUser_failure_notMatchPassword() throws Exception {
        // given
        createUser();
        UpdateUserRequestDto requestDto = new UpdateUserRequestDto(
                "12345678",
                "Admin123!",
                "서에 번쩍"
        );

        // when
        ResultActions actions = mockMvc.perform(put("/api/users")
                .content(objectMapper.writeValueAsString(requestDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        // then
        actions.andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithCustomMockUser
    @DisplayName("회원 정보 수정 실패 - 닉네임 미입력")
    public void updateUser_failure_blankNickname() throws Exception {
        // given
        createUser();
        UpdateUserRequestDto requestDto = new UpdateUserRequestDto(
                "1q2w3e4r#",
                "Admin123!",
                null
        );

        // when
        ResultActions actions = mockMvc.perform(put("/api/users")
                .content(objectMapper.writeValueAsString(requestDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        // then
        actions.andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithCustomMockUser
    @DisplayName("회원 정보 수정 실패 - 잘못된 형식의 비밀번호")
    public void updateUser_failure_badPassword() throws Exception {
        // given
        createUser();
        UpdateUserRequestDto requestDto = new UpdateUserRequestDto(
                "1q2w3e4r#",
                "12345678",
                "서에 번쩍"
        );

        // when
        ResultActions actions = mockMvc.perform(put("/api/users")
                .content(objectMapper.writeValueAsString(requestDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        // then
        actions.andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithCustomMockUser
    @DisplayName("회원 탈퇴 성공")
    public void deleteUser_success() throws Exception {
        // given
        createUser();
        PasswordCheckRequestDto requestDto = new PasswordCheckRequestDto(
                "1q2w3e4r#"
        );

        // when
        ResultActions actions = mockMvc.perform(delete("/api/users")
                .content(objectMapper.writeValueAsString(requestDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        // then
        actions.andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    @WithCustomMockUser
    @DisplayName("회원 탈퇴 실패 - 이미 탈퇴한 유저")
    public void deleteUser_failure_notFound() throws Exception {
        // given
        PasswordCheckRequestDto requestDto = new PasswordCheckRequestDto(
                "1q2w3e4r#"
        );

        // when
        ResultActions actions = mockMvc.perform(delete("/api/users")
                .content(objectMapper.writeValueAsString(requestDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        // then
        actions.andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithCustomMockUser
    @DisplayName("회원 탈퇴 실패 - 비밀번호 불일치")
    public void deleteUser_failure_notMatchPassword() throws Exception {
        // given
        createUser();
        PasswordCheckRequestDto requestDto = new PasswordCheckRequestDto(
                "12345678"
        );

        // when
        ResultActions actions = mockMvc.perform(delete("/api/users")
                .content(objectMapper.writeValueAsString(requestDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        // then
        actions.andDo(print())
                .andExpect(status().isUnauthorized());
    }
}
