package com.saebyeok.saebyeok.dto;

import com.saebyeok.saebyeok.domain.SubEmotion;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SubEmotionResponse {
    private Long id;
    private String name;

    public SubEmotionResponse(SubEmotion subEmotion) {
        this.id = subEmotion.getId();
        this.name = subEmotion.getName();
    }
}
