package com.saebyeok.saebyeok.service;

import com.saebyeok.saebyeok.domain.Article;
import com.saebyeok.saebyeok.domain.CommentRepository;
import com.saebyeok.saebyeok.domain.Member;
import com.saebyeok.saebyeok.dto.CommentCreateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {

    private CommentService commentService;

    @Mock
    private CommentRepository commentRepository;

    @BeforeEach
    void setUp() {
        this.commentService = new CommentService(commentRepository);
    }

    @DisplayName("댓글 등록 메서드를 호출했을 때, 댓글 등록을 수행한다.")
    @Test
    void createCommentTest() {
        Member member = new Member();
        LocalDateTime now = LocalDateTime.now();
        Article article = new Article();
        commentService.createComment(new CommentCreateRequest("새벽 좋아요", member, "시라소니", now,
                                                              article, false));

        verify(commentRepository).save(any());
    }
}
