package com.saebyeok.saebyeok.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.saebyeok.saebyeok.domain.Article;
import com.saebyeok.saebyeok.domain.Emotion;
import com.saebyeok.saebyeok.domain.Member;
import com.saebyeok.saebyeok.domain.SubEmotion;
import com.saebyeok.saebyeok.dto.ArticleCreateRequest;
import com.saebyeok.saebyeok.dto.ArticleResponse;
import com.saebyeok.saebyeok.dto.EmotionResponse;
import com.saebyeok.saebyeok.dto.SubEmotionResponse;
import com.saebyeok.saebyeok.exception.ArticleNotFoundException;
import com.saebyeok.saebyeok.service.ArticleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithUserDetails(userDetailsServiceBeanName = "userService", value = "123456789")
@SpringBootTest
class ArticleControllerTest {
    private static final String API = "/api";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final Emotion TEST_EMOTION_1 = new Emotion(1L, "기뻐요", "이미지 리소스");
    private static final Emotion TEST_EMOTION_2 = new Emotion(2L, "슬퍼요", "이미지 리소스");
    private static final SubEmotion TEST_SUB_EMOTION_1 = new SubEmotion(1L, "행복해요");
    private static final List<SubEmotion> TEST_SUB_EMOTIONS = Arrays.asList(TEST_SUB_EMOTION_1, new SubEmotion(2L,
                                                                                                               "설레요"));
    private static final Boolean TEST_IS_MINE = true;
    private static final Boolean TEST_IS_COMMENT_ALLOWED = true;
    private static final Long TEST_ID_1 = 1L;
    private static final String TEST_CONTENT_1 = "내용1";
    private static final Long TEST_ID_2 = 2L;
    private static final String TEST_CONTENT_2 = "내용2";
    private static final Long INVALID_ARTICLE_ID = 2L;
    private static final Integer TEST_PAGE_NUMBER = 0;
    private static final Integer TEST_PAGE_SIZE = 10;
    private static final Long TEST_LIKES_COUNT = 10L;
    private static final String INVALID_CONTENT = "이 글은 300자가 넘어가는 글입니다. 이 글은 300자가 넘어가는 글입니다. 이 글은 300자가 넘어가는 글입니다. 이 글은 300자가 넘어가는 글입니다. 이 글은 300자가 넘어가는 글입니다. 이 글은 300자가 넘어가는 글입니다. 이 글은 300자가 넘어가는 글입니다. 이 글은 300자가 넘어가는 글입니다. 이 글은 300자가 넘어가는 글입니다. 이 글은 300자가 넘어가는 글입니다. 이 글은 300자가 넘어가는 글입니다. 이 글은 300자가 넘어가는 글입니다. 이 글은 300자가 넘어가는 글입니다. 이 글은 300자가 넘어가는 글입니다.";
    public static final boolean TEST_IS_LIKED_BY_ME = false;

    private MockMvc mockMvc;
    private List<ArticleResponse> articles;

    @MockBean
    private ArticleService articleService;

    @BeforeEach
    void setUp(WebApplicationContext context) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity())
                .build();
        this.articles = new ArrayList<>();
        List<SubEmotionResponse> subEmotionResponses = TEST_SUB_EMOTIONS.stream().map(SubEmotionResponse::new).collect(Collectors.toList());
        articles.add(new ArticleResponse(TEST_ID_1, TEST_CONTENT_1, LocalDateTime.now(), new EmotionResponse(TEST_EMOTION_1), subEmotionResponses, TEST_IS_COMMENT_ALLOWED, TEST_IS_MINE, TEST_LIKES_COUNT, TEST_IS_LIKED_BY_ME, null));
        articles.add(new ArticleResponse(TEST_ID_2, TEST_CONTENT_2, LocalDateTime.of(2020, 6, 12, 5, 30, 0), new EmotionResponse(TEST_EMOTION_2), subEmotionResponses, TEST_IS_COMMENT_ALLOWED, TEST_IS_MINE, TEST_LIKES_COUNT, TEST_IS_LIKED_BY_ME, null));
    }

    @DisplayName("'/articles'로 get 요청을 보내면 글 목록 리스트를 받는다")
    @Test
    void getArticlesTest() throws Exception {
        when(articleService.getArticles(any(Member.class), eq(TEST_PAGE_NUMBER), eq(TEST_PAGE_SIZE), any())).thenReturn(Arrays.asList(articles.get(0)));

        this.mockMvc.perform(get(API + "/articles?page=" + TEST_PAGE_NUMBER + "&size=" + TEST_PAGE_SIZE).
                accept(MediaType.APPLICATION_JSON_VALUE)).
                andExpect(jsonPath("$", hasSize(1))).
                andExpect(jsonPath("$[0].content").value(TEST_CONTENT_1)).
                andExpect(jsonPath("$[0].likesCount").value(TEST_LIKES_COUNT)).
                andExpect(jsonPath("$[0].isLikedByMe").value(TEST_IS_LIKED_BY_ME));
    }

    @WithAnonymousUser
    @DisplayName("비회원이 '/articles'로 get 요청을 보내면 글 목록 리스트를 받는다")
    @Test
    void getArticlesWithGuestUserTest() throws Exception {
        when(articleService.getArticles(any(Member.class), eq(TEST_PAGE_NUMBER), eq(TEST_PAGE_SIZE), any())).thenReturn(Arrays.asList(articles.get(0)));

        this.mockMvc.perform(get(API + "/articles?page=" + TEST_PAGE_NUMBER + "&size=" + TEST_PAGE_SIZE).
                accept(MediaType.APPLICATION_JSON_VALUE)).
                andExpect(jsonPath("$", hasSize(1))).
                andExpect(jsonPath("$[0].content").value(TEST_CONTENT_1)).
                andExpect(jsonPath("$[0].likesCount").value(TEST_LIKES_COUNT)).
                andExpect(jsonPath("$[0].isLikedByMe").value(TEST_IS_LIKED_BY_ME));
    }

    @DisplayName("'/articles'로 post 요청을 보내면 글을 생성한다")
    @Test
    void createArticleTest() throws Exception {
        ArticleCreateRequest request = new ArticleCreateRequest(TEST_CONTENT_1, TEST_EMOTION_1.getId(), Collections.emptyList(), TEST_IS_COMMENT_ALLOWED);
        String requestAsString = OBJECT_MAPPER.writeValueAsString(request);
        Article article = new Article(TEST_CONTENT_1, null, TEST_IS_COMMENT_ALLOWED);

        when(articleService.createArticle(any(Member.class), any(ArticleCreateRequest.class))).thenReturn(article);

        this.mockMvc.perform(post(API + "/articles").
                content(requestAsString).
                contentType(MediaType.APPLICATION_JSON)).
                andExpect(status().isCreated()).
                andDo(print());
    }

    @DisplayName("예외 테스트: '/articles'로 post 요청을 보내는 글의 내용이 제한 글자수를 넘으면 예외가 발생한다")
    @Test
    void overLengthContentTest() throws Exception {
        ArticleCreateRequest request = new ArticleCreateRequest(INVALID_CONTENT, TEST_EMOTION_1.getId(), Collections.emptyList(), TEST_IS_COMMENT_ALLOWED);
        String requestAsString = OBJECT_MAPPER.writeValueAsString(request);

        this.mockMvc.perform(post(API + "/articles").
                content(requestAsString).
                contentType(MediaType.APPLICATION_JSON)).
                andExpect(status().isBadRequest()).
                andDo(print());
    }

    @DisplayName("'/articles/{articleId}'로 get 요청을 보내면 articleId에 해당하는 글을 받는다")
    @Test
    void readArticleTest() throws Exception {
        when(articleService.readArticle(any(Member.class), eq(TEST_ID_1))).thenReturn(articles.get(0));

        this.mockMvc.perform(get(API + "/articles/" + TEST_ID_1).
                contentType(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk()).
                andExpect(jsonPath("$.id").value(TEST_ID_1)).
                andExpect(jsonPath("$.content").value(TEST_CONTENT_1)).
                andExpect(jsonPath("$.emotion.name").value(TEST_EMOTION_1.getName())).
                andExpect(jsonPath("$.subEmotions[0].name").value(TEST_SUB_EMOTION_1.getName())).
                andExpect(jsonPath("$.isCommentAllowed").value(TEST_IS_COMMENT_ALLOWED)).
                andExpect(jsonPath("$.likesCount").value(TEST_LIKES_COUNT)).
                andExpect(jsonPath("$.isLikedByMe").value(TEST_IS_LIKED_BY_ME));
    }

    @WithAnonymousUser
    @DisplayName("비회원이 '/articles/{articleId}'로 get 요청을 보내면 articleId에 해당하는 글을 받는다")
    @Test
    void readArticleWithGuestUserTest() throws Exception {
        when(articleService.readArticle(any(Member.class), eq(TEST_ID_1))).thenReturn(articles.get(0));

        this.mockMvc.perform(get(API + "/articles/" + TEST_ID_1).
                contentType(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk()).
                andExpect(jsonPath("$.id").value(TEST_ID_1)).
                andExpect(jsonPath("$.content").value(TEST_CONTENT_1)).
                andExpect(jsonPath("$.emotion.name").value(TEST_EMOTION_1.getName())).
                andExpect(jsonPath("$.subEmotions[0].name").value(TEST_SUB_EMOTION_1.getName())).
                andExpect(jsonPath("$.isCommentAllowed").value(TEST_IS_COMMENT_ALLOWED)).
                andExpect(jsonPath("$.likesCount").value(TEST_LIKES_COUNT)).
                andExpect(jsonPath("$.isLikedByMe").value(TEST_IS_LIKED_BY_ME));
    }

    @DisplayName("예외 테스트: '/articles/{articleId}'로 get 요청을 보냈을 때 articleId가 존재하지 않으면 ArticleNotFoundException이 발생한다")
    @Test
    void readArticleExceptionTest() throws Exception {
        when(articleService.readArticle(any(Member.class), eq(INVALID_ARTICLE_ID)))
                .thenThrow(ArticleNotFoundException.class);

        this.mockMvc.perform(get(API + "/articles/" + INVALID_ARTICLE_ID).
                contentType(MediaType.APPLICATION_JSON)).
                andExpect(status().isBadRequest());
    }

    @DisplayName("'/articles/{articleId}'로 delete 요청을 보내면 articleId에 해당하는 글을 삭제한다")
    @Test
    void deleteArticleTest() throws Exception {
        doNothing().when(articleService).deleteArticle(any(Member.class), eq(TEST_ID_1));

        this.mockMvc.perform(delete(API + "/articles/" + TEST_ID_1)).
                andExpect(status().isNoContent());
    }

    @DisplayName("예외 테스트: '/articles/{articleId}'로 delete 요청을 보냈을 때 articleId가 존재하지 않으면 ArticleNotFoundException이 발생한다")
    @Test
    void deleteArticleExceptionTest() throws Exception {
        doThrow(new ArticleNotFoundException(INVALID_ARTICLE_ID))
                .when(articleService).deleteArticle(any(Member.class), eq(INVALID_ARTICLE_ID));

        this.mockMvc.perform(delete(API + "/articles/" + INVALID_ARTICLE_ID).
                contentType(MediaType.APPLICATION_JSON)).
                andExpect(status().isBadRequest());
    }

    @DisplayName("'/member/articles'로 get 요청을 보내면 사용자가 작성한 글 목록 리스트를 작성 날짜와 상관없이 받는다")
    @Test
    void memberArticleLoadTest() throws Exception {
        when(articleService.getMemberArticles(any(), eq(TEST_PAGE_NUMBER), eq(TEST_PAGE_SIZE), any())).thenReturn(articles);

        this.mockMvc.perform(get(API + "/member/articles?page=" + TEST_PAGE_NUMBER + "&size=" + TEST_PAGE_SIZE).
                accept(MediaType.APPLICATION_JSON_VALUE)).
                andExpect(status().isOk()).
                andExpect(jsonPath("$", hasSize(2))).
                andExpect(jsonPath("$[0].content").value(TEST_CONTENT_1)).
                andExpect(jsonPath("$[0].likesCount").value(TEST_LIKES_COUNT)).
                andExpect(jsonPath("$[0].isLikedByMe").value(TEST_IS_LIKED_BY_ME)).
                andExpect(jsonPath("$[1].content").value(TEST_CONTENT_2));
    }

    @DisplayName("'/member/articles/{articleId}'로 get 요청을 보내면 articleId에 해당하는 글을 받는다")
    @Test
    void memberArticleReadTest() throws Exception {
        when(articleService.readMemberArticle(any(Member.class), eq(TEST_ID_2))).thenReturn(articles.get(1));

        this.mockMvc.perform(get(API + "/member/articles/" + TEST_ID_2).
                contentType(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk()).
                andExpect(jsonPath("$.id").value(TEST_ID_2)).
                andExpect(jsonPath("$.content").value(TEST_CONTENT_2)).
                andExpect(jsonPath("$.emotion.name").value(TEST_EMOTION_2.getName())).
                andExpect(jsonPath("$.subEmotions[0].name").value(TEST_SUB_EMOTION_1.getName())).
                andExpect(jsonPath("$.isCommentAllowed").value(TEST_IS_COMMENT_ALLOWED)).
                andExpect(jsonPath("$.likesCount").value(TEST_LIKES_COUNT)).
                andExpect(jsonPath("$.isLikedByMe").value(TEST_IS_LIKED_BY_ME));
    }
}
