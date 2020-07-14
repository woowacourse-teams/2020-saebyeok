package com.saebyeok.saebyeok.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.saebyeok.saebyeok.domain.Member;
import com.saebyeok.saebyeok.dto.ArticleCreateRequest;
import com.saebyeok.saebyeok.dto.ArticleResponse;
import com.saebyeok.saebyeok.service.ArticleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(value = {ArticleController.class})
class ArticleControllerTest {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final String TEST_CONTENT = "내용";
    private static final String TEST_EMOTION = "기쁨";
    private static final boolean TEST_IS_COMMENT_ALLOWED = true;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ArticleService articleService;

    private ArticleResponse articleResponse;

    @BeforeEach
    void setUp() {
        articleResponse = new ArticleResponse(1L, TEST_CONTENT, LocalDateTime.now(), TEST_EMOTION, TEST_IS_COMMENT_ALLOWED, null);
    }

    @DisplayName("'/articles'로 get 요청을 보내면 글 목록 리스트를 받는다.")
    @Test
    void getArticlesTest() throws Exception {
        when(articleService.getArticles()).thenReturn(Arrays.asList(articleResponse));

        this.mockMvc.perform(get("/articles").
                accept(MediaType.APPLICATION_JSON_VALUE)).
                andExpect(jsonPath("$", hasSize(1))).
                andExpect(jsonPath("$[0].content").value("내용"));
    }

    @DisplayName("'/articles'로 post 요청을 보내면 글을 생성한다.")
    @Test
    void createArticleTest() throws Exception {
        ArticleCreateRequest request = new ArticleCreateRequest(TEST_CONTENT, TEST_EMOTION, TEST_IS_COMMENT_ALLOWED);
        String requestAsString = OBJECT_MAPPER.writeValueAsString(request);

        when(articleService.createArticle(any(Member.class), any(ArticleCreateRequest.class))).thenReturn(request.toArticle(new Member()));

        this.mockMvc.perform(post("/articles").
                content(requestAsString).
                contentType(MediaType.APPLICATION_JSON)).
                andExpect(status().isCreated()).
                andDo(print());
    }
}
