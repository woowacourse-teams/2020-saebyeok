package com.saebyeok.saebyeok.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ArticlesAnalysisResponse {
    private List<Integer> articleEmotionsCount;
    private Long mostEmotionId;
}
