package com.saebyeok.saebyeok.service;

import com.saebyeok.saebyeok.domain.*;
import com.saebyeok.saebyeok.dto.CommentCreateRequest;
import com.saebyeok.saebyeok.dto.CommentResponse;
import com.saebyeok.saebyeok.exception.ArticleNotFoundException;
import com.saebyeok.saebyeok.exception.CommentNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static java.util.Optional.ofNullable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {
    private static final String COMMENT_CONTENT = "댓글 내용입니다.";
    private static final Long INVALID_ARTICLE_ID = 100L;
    private static final Long INVALID_COMMENT_ID = 100L;
    private static final Long ARTICLE_ID = 1L;

    private CommentService commentService;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private ArticleRepository articleRepository;

    @Mock
    private NicknameGenerator nicknameGenerator;

    private Member member;
    private Article article;
    private Comment comment;

    @BeforeEach
    void setUp() {
        this.commentService = new CommentService(commentRepository, articleRepository, nicknameGenerator);
        this.member = new Member();
        this.article = new Article();
        this.comment = new Comment("테스트", null, "슬픈돌고래", null, null);
    }

    @DisplayName("댓글 등록 메서드를 호출했을 때, 댓글 등록을 수행한다")
    @Test
    void createCommentTest() {
        CommentCreateRequest commentCreateRequest = new CommentCreateRequest("새벽 좋아요", 1L, null);

        Long articleId = commentCreateRequest.getArticleId();
        when(articleRepository.findById(articleId)).thenReturn(ofNullable(article));
        commentService.createComment(member, commentCreateRequest);

        verify(commentRepository).save(any());
    }

    @DisplayName("예외 테스트: 존재하지 않는 게시글에 댓글 작성 요청을 하면 예외가 발생해야 한다.")
    @Test
    void invalidRequestTest1() {
        when(articleRepository.findById(INVALID_ARTICLE_ID)).thenReturn(Optional.empty());

        CommentCreateRequest request = new CommentCreateRequest(COMMENT_CONTENT, INVALID_ARTICLE_ID, null);

        assertThatThrownBy(() -> commentService.createComment(member, request))
                .isInstanceOf(ArticleNotFoundException.class)
                .hasMessage(INVALID_ARTICLE_ID + "에 해당하는 게시글을 찾을 수 없습니다.");
    }

    @DisplayName("예외 테스트: 존재하지 않는 댓글에 대댓글 작성 요청을 하면 예외가 발생해야 한다.")
    @Test
    void invalidRequestTest2() {
        when(articleRepository.findById(ARTICLE_ID)).thenReturn(Optional.of(new Article()));
        when(commentRepository.findById(INVALID_COMMENT_ID)).thenReturn(Optional.empty());

        CommentCreateRequest request = new CommentCreateRequest(COMMENT_CONTENT, ARTICLE_ID, INVALID_COMMENT_ID);

        assertThatThrownBy(() -> commentService.createComment(member, request))
                .isInstanceOf(CommentNotFoundException.class)
                .hasMessage(INVALID_COMMENT_ID + "에 해당하는 댓글을 찾을 수 없습니다.");
    }

    @DisplayName("댓글 조회 메서드를 호출했을 때, 댓글 조회를 수행한다.")
    @Test
    void getCommentsTest() {
        when(commentRepository.findAllByArticleId(anyLong())).thenReturn(Arrays.asList(comment));
        List<CommentResponse> comments = commentService.getComment(member, ARTICLE_ID);

        assertThat(comments).hasSize(1);
        assertThat(comments.get(0).getContent()).isEqualTo(comment.getContent());
        assertThat(comments.get(0).getNickname()).isEqualTo(comment.getNickname());
        assertFalse(comments.get(0).getIsDeleted());
    }

    @DisplayName("특정 사용자의 삭제하지 않은 댓글 개수 조회 메서드를 호출했을 때, 댓글 개수 조회를 수행한다")
    @Test
    void countTotalCommentsByTest() {
        Long expectedCommentsSize = 3L;
        when(commentRepository.countCommentsByMemberAndIsDeleted(any(Member.class), anyBoolean())).thenReturn(expectedCommentsSize);

        Long commentsSize = commentService.countTotalCommentsBy(member);

        assertThat(commentsSize).isEqualTo(expectedCommentsSize);
    }

    @DisplayName("특정 사용자의 댓글 조회 메서드를 호출했을 때, 댓글 조회를 수행한다.")
    @Test
    void findAllCommentsByTest() {
        when(commentRepository.findAllByMemberAndIsDeleted(any(Member.class), anyBoolean())).thenReturn(Arrays.asList(comment));
        List<Comment> comments = commentService.findAllCommentsBy(member);

        assertThat(comments).hasSize(1);
        assertThat(comments.get(0).getContent()).isEqualTo(comment.getContent());
        assertThat(comments.get(0).getNickname()).isEqualTo(comment.getNickname());
        assertFalse(comments.get(0).getIsDeleted());
    }

    @DisplayName("댓글 삭제 메서드를 호출했을 때, 댓글 삭제를 수행한다")
    @Test
    void deleteCommentTest() throws IllegalAccessException {
        Long savedCommentId = 1L;
        when(commentRepository.findById(savedCommentId)).thenReturn(Optional.of(comment));

        commentService.deleteComment(comment.getMember(), savedCommentId);

        verify(commentRepository).save(any());
    }

    @DisplayName("특정 게시물의 댓글 개수 조회 메서드를 호출했을 때, 댓글 개수 조회를 수행한다")
    @Test
    void countCommentsTest() {
        Long articleId = 1L;
        Long expectedCommentsSize = 3L;
        when(commentRepository.countCommentsByArticleId(articleId)).thenReturn(expectedCommentsSize);

        Long commentsSize = commentService.countComments(articleId);

        assertThat(commentsSize).isEqualTo(expectedCommentsSize);
    }
}
