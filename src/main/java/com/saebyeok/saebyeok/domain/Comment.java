package com.saebyeok.saebyeok.domain;

import com.saebyeok.saebyeok.exception.InvalidLengthCommentException;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
public class Comment {
    public static final int MIN_LENGTH = 1;
    public static final int MAX_LENGTH = 140;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 140, nullable = false)
    private String content;

    @ManyToOne
    private Member member;
    private String nickname;

    @CreatedDate
    private LocalDateTime createdDate;

    @ManyToOne
    private Article article;
    private Boolean isDeleted;

    @Builder
    public Comment(String content, String nickname, Boolean isDeleted) {
        validateLength(content);

        this.content = content;
        this.nickname = nickname;
        this.isDeleted = isDeleted;
    }

    private void validateLength(String content) {
        int contentLength = content.trim().length();
        if (contentLength < MIN_LENGTH || contentLength > MAX_LENGTH) {
            throw new InvalidLengthCommentException(contentLength);
        }
    }
}
