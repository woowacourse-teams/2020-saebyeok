package com.saebyeok.saebyeok.acceptance;

import com.saebyeok.saebyeok.dto.EmotionDetailResponse;
import com.saebyeok.saebyeok.dto.EmotionResponse;
import com.saebyeok.saebyeok.dto.ExceptionResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class EmotionAcceptanceTest extends AcceptanceTest {
    private static final Long NOT_EXIST_EMOTION_ID = 10000L;

    /**
     * Scenario: Emotion 및 SubEmotion을 조회할 수 있다.
     * <p>
     * given DB에 Emotion이 저장되어 있다.
     * when Emotion의 목록을 요청한다.
     * then Emotion의 목록을 반환한다.
     * <p>
     * when 특정 Emotion을 조회한다.
     * then 해당 Emotion의 정보를 반환한다.
     * <p>
     * when 존재하지 않는 Emotion의 정보를 조회한다.
     * then 조회에 실패한다.
     **/

    @DisplayName("감정 도메인에 대해 요청을 보낼 때, 응답이 올바르게 수행되어야 한다")
    @Test
    void manageEmotion() {
        //when Emotion의 목록을 요청한다.
        List<EmotionResponse> emotions = getEmotions();

        //then Emotion의 목록을 반환한다.
        assertThat(emotions).
                hasSize(3).
                extracting("id").
                containsOnly(1L, 2L, 3L);

        //when 특정 Emotion을 조회한다.
        EmotionDetailResponse emotion = readEmotion(1L);

        //then 해당 Emotion의 정보를 반환한다.
        assertThat(emotion).hasFieldOrPropertyWithValue("name", "기뻐요").
                hasFieldOrPropertyWithValue("imageResource", "리소스");
        assertThat(emotion.getSubEmotions()).
                hasSize(2).
                extracting("id").
                containsOnly(1L, 2L);

        //when 존재하지 않는 Emotion의 정보를 조회한다.
        ExceptionResponse emotionNotFoundExceptionResponse = getNotFoundEmotion();

        //then 조회에 실패한다.
        assertThat(emotionNotFoundExceptionResponse.getErrorMessage()).contains("에 해당하는 감정 대분류를 찾을 수 없습니다.");
    }

    private List<EmotionResponse> getEmotions() {
        //@formatter:off
        return
                given().
                        auth().oauth2(TOKEN).
                when().
                        get(API + "/emotions").
                then().
                        log().all().
                        extract().
                        jsonPath().
                        getList(".", EmotionResponse.class);
        //@formatter:on
    }

    private EmotionDetailResponse readEmotion(Long id) {
        //@formatter:off
        return
                given().
                        auth().oauth2(TOKEN).
                        accept(MediaType.APPLICATION_JSON_VALUE).
                when().
                        get(API + "/emotions/" + id).
                then().
                        log().all().
                        extract().
                        as(EmotionDetailResponse.class);
        //@formatter:on
    }

    private ExceptionResponse getNotFoundEmotion() {
        //@formatter:off
        return
                given().
                        auth().oauth2(TOKEN).
                when().
                        get(API + "/emotions/" + NOT_EXIST_EMOTION_ID).
                then().
                        log().all().
                        statusCode(HttpStatus.BAD_REQUEST.value()).
                        extract().as(ExceptionResponse.class);
        //@formatter:on
    }
}
