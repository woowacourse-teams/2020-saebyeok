package com.saebyeok.saebyeok.service;

import com.saebyeok.saebyeok.domain.*;
import com.saebyeok.saebyeok.dto.CommentCreateRequest;
import com.saebyeok.saebyeok.exception.InvalidLengthCommentException;
import com.saebyeok.saebyeok.util.NicknameGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static java.util.Optional.ofNullable;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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

    @DisplayName("예외 테스트: 최소 길이보다 짧은 댓글 등록 메서드를 호출했을 때, 예외가 발생한다")
    @Test
    void createUnderLengthCommentTest() {
        CommentCreateRequest commentCreateRequest = createCommentCreateRequest(" ");
        Long articleId = commentCreateRequest.getArticleId();
        when(articleRepository.findById(articleId)).thenReturn(ofNullable(article));

        assertThatThrownBy(() -> commentService.createComment(member, commentCreateRequest)).
                isInstanceOf(InvalidLengthCommentException.class).
                hasMessageContaining("올바르지 않은 댓글입니다!");
    }

    @DisplayName("예외 테스트: 최대 길이보다 긴 댓글 등록 메서드를 호출했을 때, 예외가 발생한다")
    @Test
    void createOverLengthCommentTest() {
        String content = "나만 잘되게 해주세요(강보라 지음·인물과사상사)=자존감이 높은 사람과 ‘관종’의 차이는 무엇일까? " +
                "‘취향 존중’이 유행하고, ‘오이를 싫어하는 사람들의 모임’이 생기는 이유는 뭘까? 이 시대 새로운 지위를 차지하고 있는 ‘개인’에 관한 탐구 보고서. " +
                "1만4000원.\n";
        CommentCreateRequest commentCreateRequest = createCommentCreateRequest(content);
        Long articleId = commentCreateRequest.getArticleId();
        when(articleRepository.findById(articleId)).thenReturn(ofNullable(article));

        assertThatThrownBy(() -> commentService.createComment(member, commentCreateRequest)).
                isInstanceOf(InvalidLengthCommentException.class).
                hasMessageContaining("올바르지 않은 댓글입니다!");
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
