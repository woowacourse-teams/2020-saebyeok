package com.saebyeok.saebyeok.controller;

import com.saebyeok.saebyeok.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;

import static com.saebyeok.saebyeok.acceptance.MemberAcceptanceTest.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MemberController.class)
class MemberControllerTest {
    @MockBean
    private MemberService memberService;
    @Autowired
    private MockMvc mockMvc;

    @DisplayName("유효한 소셜 로그인 토큰으로 로그인을 하면 토큰을 리턴받는다.")
    @Test
    void login() throws Exception {
        when(memberService.createToken(any())).thenReturn("saebyeokToken");

        HashMap<String, String> request = new HashMap<>();
        request.put("snsToken", SNS_TOKEN);
        String content = OBJECT_MAPPER.writeValueAsString(request);

        this.mockMvc.perform(post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(content))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tokenType").value("Bearer"));
    }

    @DisplayName("예외테스트: 유효하지 않은 소셜 로그인 토큰으로 로그인을 하면 예외가 발생한다.")
    @Test
    void loginExceptionTest() throws Exception {
        HashMap<String, String> request = new HashMap<>();
        request.put("snsToken", INVALID_SNS_TOKEN);
        String content = OBJECT_MAPPER.writeValueAsString(request);

        this.mockMvc.perform(post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(content))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessage").value("유효하지 않은 토큰입니다: " + INVALID_SNS_TOKEN));
    }
}