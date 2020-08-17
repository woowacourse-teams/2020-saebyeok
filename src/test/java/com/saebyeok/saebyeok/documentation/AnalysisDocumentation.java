package com.saebyeok.saebyeok.documentation;

import com.saebyeok.saebyeok.documentation.common.Documentation;
import com.saebyeok.saebyeok.domain.Member;
import com.saebyeok.saebyeok.dto.ArticlesAnalysisResponse;
import com.saebyeok.saebyeok.dto.CommentsAnalysisResponse;
import com.saebyeok.saebyeok.dto.TokenResponse;
import com.saebyeok.saebyeok.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@WithUserDetails(userDetailsServiceBeanName = "userService", value = "a@a.com")
public class AnalysisDocumentation extends Documentation {
    protected TokenResponse tokenResponse;
    private MockMvc mockMvc;

    @MockBean
    private EmotionService emotionService;

    @MockBean
    private ArticleService articleService;

    @MockBean
    private ArticleEmotionService articleEmotionService;

    @MockBean
    private CommentService commentService;

    @MockBean
    private AnalysisService analysisService;

    @BeforeEach
    public void setUp(WebApplicationContext context, RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(documentationConfiguration(restDocumentation))
                .apply(springSecurity())
                .build();
        tokenResponse = new TokenResponse("token", "bearer");
    }

    @Test
    void getArticlesAnalysis() throws Exception {
        int[] articleEmotionsCount = {5, 3, 4, 0, 1, 1};
        String articleAnalysisMessage = "기쁜 일이 많았네요! 앞으로도 행복한 일이 가득하기를 바랄게요~";
        ArticlesAnalysisResponse response = new ArticlesAnalysisResponse(articleEmotionsCount, articleAnalysisMessage);

        given(emotionService.getAllEmotionsIds()).willReturn(new ArrayList<>());
        given(articleService.getMemberArticlesIds(any(Member.class))).willReturn(new ArrayList<>());
        given(articleEmotionService.findArticleEmotionsCount(any(), any())).willReturn(articleEmotionsCount);
        given(articleService.getMemberArticlesIdsByCutDate(any(Member.class), any())).willReturn(new ArrayList<>());
        given(articleEmotionService.findMostEmotionIdInArticles(any(), any())).willReturn(1L);
        given(analysisService.findArticlesAnalysisMessage(any())).willReturn(articleAnalysisMessage);

        this.mockMvc.perform(get("/api/analysis/articles").
                header("Authorization", tokenResponse.getTokenType() + " " + tokenResponse.getAccessToken()).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk()).
                andDo(print()).
                andDo(document("analysis/articles",
                               getDocumentRequest(),
                               getDocumentResponse(),
                               requestHeaders(
                                       headerWithName("Authorization").description("Bearer auth credentials")
                               ),
                               responseFields(
                                       fieldWithPath("articleEmotionsCount").type(JsonFieldType.ARRAY)
                                               .description("감정 대분류 별 사용자가 작성한 게시글의 개수 (감정 대분류 순서대로 배열)"),
                                       fieldWithPath("articleAnalysisMessage").type(JsonFieldType.STRING)
                                               .description("사용자가 최근 일정 기간 동안 작성한 게시글의 감정 대분류 경향을 분석한 메세지")
                               )
                ));
    }

    @Test
    void getCommentsAnalysis() throws Exception {
        int totalCommentsCount = 77;
        CommentsAnalysisResponse response = new CommentsAnalysisResponse(totalCommentsCount);

        given(commentService.findTotalCommentsCountByMember(any(Member.class))).willReturn(totalCommentsCount);

        this.mockMvc.perform(get("/api/analysis/comments").
                header("Authorization", tokenResponse.getTokenType() + " " + tokenResponse.getAccessToken()).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk()).
                andDo(print()).
                andDo(document("analysis/comments",
                               getDocumentRequest(),
                               getDocumentResponse(),
                               requestHeaders(
                                       headerWithName("Authorization").description("Bearer auth credentials")
                               ),
                               responseFields(
                                       fieldWithPath("totalCommentsCount").type(JsonFieldType.NUMBER)
                                               .description("사용자가 작성한 댓글의 총 개수")
                               )
                ));
    }
}
