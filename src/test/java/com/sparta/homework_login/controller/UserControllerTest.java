package com.sparta.homework_login.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.homework_login.dto.request.SignUpRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Rollback
@Transactional
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private SignUpRequestDto createSignUpRequestDto(String username, String password, String nickname) {
        return SignUpRequestDto.builder()
                .username(username)
                .password(password)
                .nickname(nickname)
                .build();
    }

    @Test
    @DisplayName("회원가입 성공")
    public void signUp_success() throws Exception {
        // given
        SignUpRequestDto requestDto = createSignUpRequestDto(
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
        SignUpRequestDto requestDto = createSignUpRequestDto(
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
}
