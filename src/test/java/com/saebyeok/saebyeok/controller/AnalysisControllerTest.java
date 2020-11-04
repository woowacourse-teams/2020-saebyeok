package com.saebyeok.saebyeok.controller;

import com.saebyeok.saebyeok.domain.Member;
import com.saebyeok.saebyeok.dto.ArticlesAnalysisResponse;
import com.saebyeok.saebyeok.dto.CommentsAnalysisResponse;
import com.saebyeok.saebyeok.service.AnalysisService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithUserDetails(userDetailsServiceBeanName = "userService", value = "123456789")
@SpringBootTest
public class AnalysisControllerTest {
    private static final String API = "/api";

    private MockMvc mockMvc;

    @MockBean
    private AnalysisService analysisService;

    @BeforeEach
    void setUp(WebApplicationContext context) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @DisplayName("'/analysis/articles'로 get 요청을 보내면 게시물에 대한 분석 정보를 받는다")
    @Test
    void getArticlesAnalysisTest() throws Exception {
        List<Integer> articleEmotionsCount = Arrays.asList(1, 2, 3, 4, 5, 6);
        Long mostEmotionId = 6L;
        ArticlesAnalysisResponse articlesAnalysisResponse = new ArticlesAnalysisResponse(articleEmotionsCount, mostEmotionId);
        when(analysisService.getArticlesAnalysis(any(Member.class))).thenReturn(articlesAnalysisResponse);

        this.mockMvc.perform(get(API + "/analysis/articles").
                accept(MediaType.APPLICATION_JSON_VALUE)).
                andExpect(status().isOk()).
                andExpect(jsonPath("$.articleEmotionsCount", hasSize(articleEmotionsCount.size()))).
                andExpect(jsonPath("$.mostEmotionId").value(mostEmotionId));
    }

    @DisplayName("'/analysis/comments'로 get 요청을 보내면 댓글에 대한 분석 정보를 받는다")
    @Test
    void getCommentsAnalysisTest() throws Exception {
        Long totalCommentsCount = 1L;
        Long totalCommentLikesCount = 3L;
        CommentsAnalysisResponse commentsAnalysisResponse = new CommentsAnalysisResponse(totalCommentsCount, totalCommentLikesCount);
        when(analysisService.getCommentsAnalysis(any(Member.class))).thenReturn(commentsAnalysisResponse);

        this.mockMvc.perform(get(API + "/analysis/comments").
                accept(MediaType.APPLICATION_JSON_VALUE)).
                andExpect(status().isOk()).
                andExpect(jsonPath("$.totalCommentsCount").value(totalCommentsCount)).
                andExpect(jsonPath("$.totalCommentLikesCount").value(totalCommentLikesCount));
    }
}
