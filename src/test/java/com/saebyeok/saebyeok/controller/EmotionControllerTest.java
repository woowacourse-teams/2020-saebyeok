package com.saebyeok.saebyeok.controller;

import com.saebyeok.saebyeok.dto.EmotionDetailResponse;
import com.saebyeok.saebyeok.dto.EmotionResponse;
import com.saebyeok.saebyeok.dto.SubEmotionResponse;
import com.saebyeok.saebyeok.exception.EmotionNotFoundException;
import com.saebyeok.saebyeok.service.EmotionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class EmotionControllerTest {
    private static final String API = "/api";
    private static final Long TEST_ID = 1L;
    private static final String TEST_NAME = "기뻐요";
    private static final String TEST_IMAGE_RESOURCE = "리소스";
    private static final Long TEST_SUBEMOTION_FIRST_ID = 1L;
    private static final Long TEST_SUBEMOTION_SECOND_ID = 2L;
    private static final String TEST_SUBEMOTION_NAME = "이름";
    private static final Long INVALID_EMOTION_ID = 1000L;

    @MockBean
    private EmotionService emotionService;

    @Autowired
    private MockMvc mockMvc;

    private EmotionResponse emotionResponse;
    private EmotionDetailResponse emotionDetailResponse;

    @BeforeEach
    void setUp() {
        this.emotionResponse = new EmotionResponse(TEST_ID, TEST_NAME, TEST_IMAGE_RESOURCE);

        List<SubEmotionResponse> subEmotions = new ArrayList<>();
        subEmotions.add(new SubEmotionResponse(TEST_SUBEMOTION_FIRST_ID, TEST_SUBEMOTION_NAME));
        subEmotions.add(new SubEmotionResponse(TEST_SUBEMOTION_SECOND_ID, TEST_SUBEMOTION_NAME));
        this.emotionDetailResponse = new EmotionDetailResponse(TEST_ID, TEST_NAME, TEST_IMAGE_RESOURCE, subEmotions);
    }

    @DisplayName("'/emotions'로 get 요청을 보내면 Emotion 리스트를 받는다")
    @Test
    void getEmotionsTest() throws Exception {
        when(emotionService.getEmotions()).thenReturn(Arrays.asList(this.emotionResponse));

        this.mockMvc.perform(get(API + "/emotions").
            accept(MediaType.APPLICATION_JSON_VALUE)).
            andExpect(jsonPath("$", hasSize(1))).
            andExpect(jsonPath("$[0].name").value(TEST_NAME));
    }

    @DisplayName("ID로 개별 Emotion 조회를 요청하면 해당 Emotion을 전달 받는다")
    @Test
    void readEmotionTest() throws Exception {
        when(emotionService.readEmotion(TEST_ID)).thenReturn(emotionDetailResponse);

        this.mockMvc.perform(get(API + "/emotions/" + TEST_ID).
            contentType(MediaType.APPLICATION_JSON)).
            andExpect(status().isOk()).
            andExpect(jsonPath("$.id").value(TEST_ID)).
            andExpect(jsonPath("$.name").value(TEST_NAME)).
            andExpect(jsonPath("$.imageResource").value(TEST_IMAGE_RESOURCE)).
            andExpect(jsonPath("$.subEmotions[0].id").value(TEST_SUBEMOTION_FIRST_ID)).
            andExpect(jsonPath("$.subEmotions[0].name").value(TEST_SUBEMOTION_NAME)).
            andExpect(jsonPath("$.subEmotions[1].id").value(TEST_SUBEMOTION_SECOND_ID)).
            andExpect(jsonPath("$.subEmotions[1].name").value(TEST_SUBEMOTION_NAME));
    }

    @DisplayName("예외 테스트: 없는 ID의 Emotion 조회를 요청하면 EmotionNotFoundException이 발생한다")
    @Test
    void readNotExistEmotionExceptionTest() throws Exception {
        doThrow(new EmotionNotFoundException(INVALID_EMOTION_ID))
            .when(emotionService).readEmotion(INVALID_EMOTION_ID);

        this.mockMvc.perform(get(API + "/emotions/" + INVALID_EMOTION_ID).
            contentType(MediaType.APPLICATION_JSON)).
            andExpect(status().isBadRequest());
    }

}
