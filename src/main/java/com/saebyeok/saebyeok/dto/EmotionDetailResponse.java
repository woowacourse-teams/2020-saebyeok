package com.saebyeok.saebyeok.dto;

import com.saebyeok.saebyeok.domain.Emotion;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EmotionDetailResponse {
    private Long id;
    private String name;
    private String imageResource;
    private List<SubEmotionResponse> subEmotions;

    public EmotionDetailResponse(Emotion emotion) {
        this.id = emotion.getId();
        this.name = emotion.getName();
        this.imageResource = emotion.getImageResource();
        this.subEmotions = emotion.getSubEmotions().
                stream().
                map(SubEmotionResponse::new).
                collect(Collectors.toList());
    }
}
