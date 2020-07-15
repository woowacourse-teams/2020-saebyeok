package com.saebyeok.saebyeok.service;

import com.saebyeok.saebyeok.domain.Article;
import com.saebyeok.saebyeok.domain.CommentRepository;
import com.saebyeok.saebyeok.domain.Member;
import com.saebyeok.saebyeok.dto.CommentCreateRequest;
import com.saebyeok.saebyeok.exception.InvalidCommentException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    private CommentService commentService;

    @Mock
    private CommentRepository commentRepository;

    private Member member;
    private LocalDateTime now;
    private Article article;

    @BeforeEach
    void setUp() {
        this.commentService = new CommentService(commentRepository);
        this.member = new Member();
        this.now = LocalDateTime.now();
        this.article = new Article();
    }

    private CommentCreateRequest createCommentCreateRequest(String content) {
        return new CommentCreateRequest(content, member, "시라소니", now,
                                        article, false);
    }

    @DisplayName("댓글 등록 메서드를 호출했을 때, 댓글 등록을 수행한다")
    @Test
    void createCommentTest() {
        //given
        CommentCreateRequest commentCreateRequest = createCommentCreateRequest("새벽 좋아요");
        //when
        commentService.createComment(commentCreateRequest);

        //then
        verify(commentRepository).save(any());
    }

    @DisplayName("예외 테스트: 최소 길이보다 짧은 댓글 등록 메서드를 호출했을 때, 예외가 발생한다")
    @Test
    void createUnderLengthCommentTest() {
        //given
        CommentCreateRequest commentCreateRequest = createCommentCreateRequest(" ");
        //when
        //then
        assertThatThrownBy(() -> commentService.createComment(commentCreateRequest)).
            isInstanceOf(InvalidCommentException.class).
            hasMessageContaining("댓글의 최소 길이는");
    }

    @DisplayName("예외 테스트: 최대 길이보다 긴 댓글 등록 메서드를 호출했을 때, 예외가 발생한다")
    @Test
    void createOverLengthCommentTest() {
        //given
        String content = "나만 잘되게 해주세요(강보라 지음·인물과사상사)=자존감이 높은 사람과 ‘관종’의 차이는 무엇일까? " +
            "‘취향 존중’이 유행하고, ‘오이를 싫어하는 사람들의 모임’이 생기는 이유는 뭘까? 이 시대 새로운 지위를 차지하고 있는 ‘개인’에 관한 탐구 보고서. " +
            "1만4000원.\n";
        CommentCreateRequest commentCreateRequest = createCommentCreateRequest(content);
        //when
        //then
        assertThatThrownBy(() -> commentService.createComment(commentCreateRequest)).
            isInstanceOf(InvalidCommentException.class).
            hasMessageContaining("댓글의 최대 길이는");
    }

    @DisplayName("댓글 삭제 메서드를 호출했을 때, 댓글 삭제를 수행한다")
    @Test
    void deleteCommentTest() {
        //given
        long savedCommentId = 1L;

        //when
        commentService.deleteComment(savedCommentId);

        //then
        verify(commentRepository).deleteById(any());
    }
}
