package com.saebyeok.saebyeok.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Getter
@NoArgsConstructor
@Entity
public class SubEmotion {

    @Id
    @Column(name = "SUB_EMOTION_ID")
    private Long id;

    private String name;

    @ManyToOne
    private Emotion emotion;

    public SubEmotion(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
