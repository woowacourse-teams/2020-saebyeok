package com.saebyeok.saebyeok.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class SubEmotion {

    @Id
    @Column(name = "SUB_EMOTION_ID")
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "EMOTION_ID")
    private Emotion emotion;

    public SubEmotion(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
