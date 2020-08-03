package com.saebyeok.saebyeok.controller;

import com.saebyeok.saebyeok.dto.EmotionDetailResponse;
import com.saebyeok.saebyeok.dto.EmotionResponse;
import com.saebyeok.saebyeok.dto.SubEmotionResponse;
import com.saebyeok.saebyeok.exception.EmotionNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class EmotionController {

    @GetMapping("/emotions")
    public ResponseEntity<List<EmotionResponse>> getEmotions() {
        List<EmotionResponse> emotions = new ArrayList<>();
        emotions.add(new EmotionResponse(1L, "이름", "리소스"));
        emotions.add(new EmotionResponse(2L, "이름", "리소스"));
        emotions.add(new EmotionResponse(3L, "이름", "리소스"));

        return ResponseEntity.ok(emotions);
    }

    @GetMapping("/emotions/{emotionId}")
    public ResponseEntity<EmotionDetailResponse> readEmotion(@PathVariable Long emotionId) {
        if (emotionId > 1000) {
            throw new EmotionNotFoundException(emotionId);
        }
        List<SubEmotionResponse> subEmotions = new ArrayList<>();
        subEmotions.add(new SubEmotionResponse(1L, "이름"));
        subEmotions.add(new SubEmotionResponse(2L, "이름"));
        EmotionDetailResponse emotionDetailResponse = new EmotionDetailResponse(1L, "기뻐요", "리소스", subEmotions);

        return ResponseEntity.ok(emotionDetailResponse);
    }
}
