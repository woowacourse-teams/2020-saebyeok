package com.saebyeok.saebyeok.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class ArticleAnalysisMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "EMOTION_ID")
    private Emotion emotion;

    private String message;

    public ArticleAnalysisMessage(Emotion emotion, String message) {
        this.emotion = emotion;
        this.message = message;
    }
}
