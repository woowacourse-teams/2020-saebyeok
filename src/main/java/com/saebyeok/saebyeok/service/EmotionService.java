package com.saebyeok.saebyeok.service;

import com.saebyeok.saebyeok.dto.EmotionDetailResponse;
import com.saebyeok.saebyeok.dto.EmotionResponse;
import com.saebyeok.saebyeok.dto.SubEmotionResponse;
import com.saebyeok.saebyeok.exception.EmotionNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class EmotionService {

    public List<EmotionResponse> getEmotions() {
        List<EmotionResponse> emotions = new ArrayList<>();
        emotions.add(new EmotionResponse(1L, "이름", "리소스"));
        emotions.add(new EmotionResponse(2L, "이름", "리소스"));
        emotions.add(new EmotionResponse(3L, "이름", "리소스"));

        return emotions;
    }

    public EmotionDetailResponse readEmotion(Long emotionId) {
        if (emotionId > 1000) {
            throw new EmotionNotFoundException(emotionId);
        }
        List<SubEmotionResponse> subEmotions = new ArrayList<>();
        subEmotions.add(new SubEmotionResponse(1L, "이름"));
        subEmotions.add(new SubEmotionResponse(2L, "이름"));

        return new EmotionDetailResponse(1L, "기뻐요", "리소스", subEmotions);
    }
}
