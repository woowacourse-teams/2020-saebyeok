package com.saebyeok.saebyeok.domain;

import javax.persistence.*;

@Entity
public class ArticleSubEmotion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ARTICLE_ID")
    private Article article;

    @ManyToOne
    @JoinColumn(name = "SUB_EMOTION_ID")
    private SubEmotion subEmotion;

    public ArticleSubEmotion(Article article, SubEmotion subEmotion) {
        this.article = article;
        this.subEmotion = subEmotion;
    }
}
