package com.saebyeok.saebyeok.service;

import com.saebyeok.saebyeok.domain.*;
import com.saebyeok.saebyeok.dto.CommentCreateRequest;
import com.saebyeok.saebyeok.util.NicknameGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static java.util.Optional.ofNullable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    private CommentService commentService;

    @Mock
    private CommentRepository commentRepository;
    @Mock
    private ArticleRepository articleRepository;
    @Mock
    private NicknameGenerator nicknameGenerator;

    private Member member;
    private Article article;

    @BeforeEach
    void setUp() {
        this.commentService = new CommentService(commentRepository, articleRepository, nicknameGenerator);
        this.member = new Member();
        this.article = new Article();
    }

    private CommentCreateRequest createCommentCreateRequest(String content) {
        return new CommentCreateRequest(content, 1L, false);
    }

    @DisplayName("댓글 등록 메서드를 호출했을 때, 댓글 등록을 수행한다")
    @Test
    void createCommentTest() {
        CommentCreateRequest commentCreateRequest = createCommentCreateRequest("새벽 좋아요");

        Long articleId = commentCreateRequest.getArticleId();
        when(articleRepository.findById(articleId)).thenReturn(ofNullable(article));
        commentService.createComment(member, commentCreateRequest);

        verify(commentRepository).save(any());
    }

    @DisplayName("댓글 삭제 메서드를 호출했을 때, 댓글 삭제를 수행한다")
    @Test
    void deleteCommentTest() throws IllegalAccessException {
        when(commentRepository.findById(1L)).thenReturn(Optional.of(new Comment("테스트", "슬픈돌고래", false)));
        Long savedCommentId = 1L;

        commentService.deleteComment(any(Member.class), savedCommentId);

        verify(commentRepository).deleteById(any());
    }
}
