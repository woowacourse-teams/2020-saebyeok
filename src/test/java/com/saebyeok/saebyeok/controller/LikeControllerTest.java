package com.saebyeok.saebyeok.controller;

import com.saebyeok.saebyeok.domain.ArticleLike;
import com.saebyeok.saebyeok.domain.CommentLike;
import com.saebyeok.saebyeok.domain.Member;
import com.saebyeok.saebyeok.exception.ArticleNotFoundException;
import com.saebyeok.saebyeok.exception.CommentNotFoundException;
import com.saebyeok.saebyeok.exception.DuplicateArticleLikeException;
import com.saebyeok.saebyeok.exception.DuplicateCommentLikeException;
import com.saebyeok.saebyeok.service.LikeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithUserDetails(userDetailsServiceBeanName = "userService", value = "a@a.com")
@SpringBootTest
class LikeControllerTest {
    private static final Long ARTICLE_ID = 1L;
    public static final long INVALID_ARTICLE_ID = 100L;
    public static final long ALREADY_LIKED_ARTICLE_ID = 1L;
    public static final long COMMENT_ID = 1L;
    public static final long INVALID_COMMENT_ID = 100L;
    public static final long ALREADY_LIKED_COMMENT_ID = 1L;

    private MockMvc mockMvc;

    @MockBean
    private LikeService likeService;

    @BeforeEach
    void setUp(WebApplicationContext context) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @DisplayName("'/api/likes/article/{articleId}'로 post 요청을 보내면 해당 게시글에 공감이 추가된다")
    @Test
    void likeArticleTest() throws Exception {
        when(likeService.likeArticle(any(Member.class), eq(ARTICLE_ID))).thenReturn(new ArticleLike());

        this.mockMvc.perform(post("/api/likes/article/" + ARTICLE_ID)).
                andExpect(status().isCreated());
    }

    @DisplayName("예외 테스트: 잘못된 게시물에 공감 요청을 보내면 예외가 발생한다")
    @Test
    void likeInvalidArticleTest() throws Exception {
        when(likeService.likeArticle(any(Member.class), eq(INVALID_ARTICLE_ID))).thenThrow(ArticleNotFoundException.class);

        this.mockMvc.perform(post("/api/likes/article/" + INVALID_ARTICLE_ID)).
                andExpect(status().isBadRequest());
    }

    @DisplayName("예외 테스트: 이미 공감한 게시물에 다시 공감을 요청하면 예외가 발생한다")
    @Test
    void likeArticleAgainTest() throws Exception {
        when(likeService.likeArticle(any(Member.class), eq(ALREADY_LIKED_ARTICLE_ID))).thenThrow(DuplicateArticleLikeException.class);

        this.mockMvc.perform(post("/api/likes/article/" + ALREADY_LIKED_ARTICLE_ID)).
                andExpect(status().isBadRequest());
    }

    @DisplayName("'/api/likes/article/{articleId}'로 공감 취소 요청을 보내면 공감이 삭제된다")
    @Test
    void unlikeArticleTest() throws Exception {
        doNothing().when(likeService).unlikeArticle(any(Member.class), eq(ARTICLE_ID));

        this.mockMvc.perform((delete("/api/likes/article/" + ARTICLE_ID))).
                andExpect(status().isNoContent());
    }

    @DisplayName("'/api/likes/comment/{commentId}'로 post 요청을 보내면 해당 댓글 공감이 추가된다")
    @Test
    void likeCommentTest() throws Exception {
        when(likeService.likeComment(any(Member.class), eq(COMMENT_ID))).thenReturn(new CommentLike());

        this.mockMvc.perform(post("/api/likes/comment/" + COMMENT_ID)).
                andExpect(status().isCreated());
    }

    @DisplayName("예외 테스트: 잘못된 댓글에 공감 요청을 보내면 예외가 발생한다")
    @Test
    void likeInvalidCommentTest() throws Exception {
        when(likeService.likeComment(any(Member.class), eq(INVALID_COMMENT_ID))).thenThrow(CommentNotFoundException.class);

        this.mockMvc.perform(post("/api/likes/comment/" + INVALID_COMMENT_ID)).
                andExpect(status().isBadRequest());
    }

    @DisplayName("예외 테스트: 이미 공감한 댓글에 다시 공감을 요청하면 예외가 발생한다")
    @Test
    void likeCommentAgainTest() throws Exception {
        when(likeService.likeComment(any(Member.class), eq(ALREADY_LIKED_COMMENT_ID))).thenThrow(DuplicateCommentLikeException.class);

        this.mockMvc.perform(post("/api/likes/comment/" + ALREADY_LIKED_COMMENT_ID)).
                andExpect(status().isBadRequest());
    }
}