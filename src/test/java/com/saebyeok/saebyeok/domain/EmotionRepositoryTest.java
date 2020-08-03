package com.saebyeok.saebyeok.domain;

import com.saebyeok.saebyeok.exception.EmotionNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import javax.transaction.Transactional;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Sql("/truncate.sql")
@Transactional
@SpringBootTest
class EmotionRepositoryTest {
    @Autowired
    private EmotionRepository emotionRepository;

    private Emotion emotion1;
    private Emotion emotion2;
    private Emotion emotion3;

    @BeforeEach
    void setUp() {
        emotion1 = new Emotion(1L, "기뻐요", "리소스1");
        emotion2 = new Emotion(2L, "슬퍼요", "리소스2");
        emotion3 = new Emotion(3L, "화나요", "리소스3");
        emotionRepository.save(emotion1);
        emotionRepository.save(emotion2);
        emotionRepository.save(emotion3);
    }

    @DisplayName("전체 Emotion을 조회한다")
    @Test
    void findAllTest() {
        List<Emotion> emotions = this.emotionRepository.findAll();
        assertThat(emotions).
                hasSize(3).
                extracting("name").
                containsOnly(emotion1.getName(), emotion2.getName(), emotion3.getName());
    }

    @DisplayName("특정 Id의 Emotion을 조회한다")
    @Test
    void findByIdTest() {
        Emotion emotion = emotionRepository.findById(emotion1.getId())
                .orElseThrow(() -> new EmotionNotFoundException(emotion1.getId()));
        assertThat(emotion.getId()).isEqualTo(emotion1.getId());
        assertThat(emotion.getName()).isEqualTo(emotion1.getName());
        assertThat(emotion.getImageResource()).isEqualTo(emotion1.getImageResource());
    }
}