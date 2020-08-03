package com.saebyeok.saebyeok.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Getter
@NoArgsConstructor
@Entity
public class SubEmotion {

    @Id
    private Long id;

    private String name;

    @ManyToOne
    private Emotion emotion;
}
