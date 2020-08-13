package com.saebyeok.saebyeok.acceptance;

import com.saebyeok.saebyeok.dto.EmotionDetailResponse;
import com.saebyeok.saebyeok.dto.EmotionResponse;
import com.saebyeok.saebyeok.dto.ExceptionResponse;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@Disabled
@Sql({"/truncate.sql", "/emotion.sql"})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmotionAcceptanceTest {
    private static final String API = "/api";
    private static final Long NOT_EXIST_EMOTION_ID = 10000L;

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

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
        //given
        //when
        List<EmotionResponse> emotions = getEmotions();
        //then
        assertThat(emotions).
                hasSize(3).
                extracting("id").
                containsOnly(1L, 2L, 3L);

        //when
        EmotionDetailResponse emotion = readEmotion(1L);
        //then
        assertThat(emotion).hasFieldOrPropertyWithValue("name", "기뻐요").
                hasFieldOrPropertyWithValue("imageResource", "리소스");
        assertThat(emotion.getSubEmotions()).
                hasSize(2).
                extracting("id").
                containsOnly(1L, 2L);

        //when
        ExceptionResponse emotionNotFoundExceptionResponse = getNotFoundEmotion();
        //then
        assertThat(emotionNotFoundExceptionResponse.getErrorMessage()).contains("에 해당하는 감정 대분류를 찾을 수 없습니다.");
    }

    private List<EmotionResponse> getEmotions() {
        //@formatter:off
        return
                given().
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
                when().
                        get(API + "/emotions/" + NOT_EXIST_EMOTION_ID).
                then().
                        log().all().
                        statusCode(HttpStatus.BAD_REQUEST.value()).
                        extract().as(ExceptionResponse.class);
        //@formatter:on
    }
}
