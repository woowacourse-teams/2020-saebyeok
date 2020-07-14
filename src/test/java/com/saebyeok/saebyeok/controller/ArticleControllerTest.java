package com.saebyeok.saebyeok.controller;

import com.saebyeok.saebyeok.dto.ArticleResponse;
import com.saebyeok.saebyeok.service.ArticleService;
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
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@WebMvcTest(value = {ArticleController.class})
class ArticleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ArticleService articleService;

    @DisplayName("'/articles'로 get 요청을 보내면 글 목록 리스트를 받는다.")
    @Test
    void getArticlesTest() throws Exception {
        ArticleResponse articleResponse = new ArticleResponse(1L, "내용", LocalDateTime.now(), "기쁨", true, null);
        when(articleService.getArticles()).thenReturn(Arrays.asList(articleResponse));

        this.mockMvc.perform(get("/articles")
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].content").value("내용"));
    }
}
