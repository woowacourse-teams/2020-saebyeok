package com.saebyeok.saebyeok.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
public class Emotion {

    @Id
    private Long id;

    private String name;
    private String imageResource;

    @OneToMany(mappedBy = "emotion")
    private List<SubEmotion> subEmotions;
}
