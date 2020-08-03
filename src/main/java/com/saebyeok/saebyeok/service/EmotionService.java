package com.saebyeok.saebyeok.service;

import com.saebyeok.saebyeok.domain.Emotion;
import com.saebyeok.saebyeok.domain.EmotionRepository;
import com.saebyeok.saebyeok.dto.EmotionDetailResponse;
import com.saebyeok.saebyeok.dto.EmotionResponse;
import com.saebyeok.saebyeok.exception.EmotionNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class EmotionService {

    private final EmotionRepository emotionRepository;

    public List<EmotionResponse> getEmotions() {
        return emotionRepository.findAll().
            stream().
            map(EmotionResponse::new).
            collect(Collectors.toList());
    }

    public EmotionDetailResponse readEmotion(Long emotionId) {
        Emotion emotion = emotionRepository.findById(emotionId).
            orElseThrow(() -> new EmotionNotFoundException(emotionId));

        return new EmotionDetailResponse(emotion);
    }
}
