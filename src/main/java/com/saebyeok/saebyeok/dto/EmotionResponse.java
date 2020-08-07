package com.saebyeok.saebyeok.dto;

import com.saebyeok.saebyeok.domain.Emotion;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EmotionResponse {
    private Long id;
    private String name;
    private String imageResource;

    public EmotionResponse(Emotion emotion) {
        this.id = emotion.getId();
        this.name = emotion.getName();
        this.imageResource = emotion.getImageResource();
    }
}
