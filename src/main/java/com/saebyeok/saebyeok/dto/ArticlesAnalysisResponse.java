package com.saebyeok.saebyeok.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ArticlesAnalysisResponse {
    private int[] articleEmotionsCount;
    private String articleAnalysisMessage;
}
