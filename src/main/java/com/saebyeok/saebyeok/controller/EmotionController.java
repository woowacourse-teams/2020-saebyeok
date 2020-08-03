package com.saebyeok.saebyeok.controller;

import com.saebyeok.saebyeok.dto.EmotionDetailResponse;
import com.saebyeok.saebyeok.dto.EmotionResponse;
import com.saebyeok.saebyeok.service.EmotionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class EmotionController {
    private final EmotionService emotionService;

    @GetMapping("/emotions")
    public ResponseEntity<List<EmotionResponse>> getEmotions() {
        List<EmotionResponse> emotions = emotionService.getEmotions();

        return ResponseEntity.ok(emotions);
    }

    @GetMapping("/emotions/{emotionId}")
    public ResponseEntity<EmotionDetailResponse> readEmotion(@PathVariable Long emotionId) {
        EmotionDetailResponse emotionDetailResponse = emotionService.readEmotion(emotionId);

        return ResponseEntity.ok(emotionDetailResponse);
    }
}
