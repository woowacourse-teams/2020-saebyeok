package com.saebyeok.saebyeok.documentation;

import com.saebyeok.saebyeok.documentation.common.Documentation;
import com.saebyeok.saebyeok.domain.Member;
import com.saebyeok.saebyeok.dto.ArticlesAnalysisResponse;
import com.saebyeok.saebyeok.dto.CommentsAnalysisResponse;
import com.saebyeok.saebyeok.dto.TokenResponse;
import com.saebyeok.saebyeok.service.AnalysisService;
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

import java.util.Arrays;
import java.util.List;

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
@WithUserDetails(userDetailsServiceBeanName = "userService", value = "123456789")
class AnalysisDocumentation extends Documentation {
    protected TokenResponse tokenResponse;
    private MockMvc mockMvc;

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
        List<Integer> articleEmotionsCount = Arrays.asList(5, 3, 4, 0, 1, 1);
        Long mostEmotionId = 1L;
        ArticlesAnalysisResponse articlesAnalysisResponse = new ArticlesAnalysisResponse(articleEmotionsCount, mostEmotionId);

        given(analysisService.getArticlesAnalysis(any(Member.class))).willReturn(articlesAnalysisResponse);

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
                                fieldWithPath("articleEmotionsCount").type(JsonFieldType.ARRAY).description("감정 대분류 별 사용자가 작성한 게시글의 개수 (감정 대분류 순서대로 배열)"),
                                fieldWithPath("mostEmotionId").type(JsonFieldType.NUMBER).description("사용자가 최근 일정 기간 동안 작성한 게시글 중 가장 많은 감정 대분류의 ID")
                        )
                ));
    }

    @Test
    void getCommentsAnalysis() throws Exception {
        Long totalCommentsCount = 42L;
        Long totalCommentLikesCount = 7L;
        CommentsAnalysisResponse commentsAnalysisResponse = new CommentsAnalysisResponse(totalCommentsCount, totalCommentLikesCount);
        given(analysisService.getCommentsAnalysis(any(Member.class))).willReturn(commentsAnalysisResponse);

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
                                fieldWithPath("totalCommentsCount").type(JsonFieldType.NUMBER).description("사용자가 작성한 댓글의 총 개수"),
                                fieldWithPath("totalCommentLikesCount").type(JsonFieldType.NUMBER).description("사용자가 작성한 댓글에 받은 공감의 총 개수")
                        )
                ));
    }
}
