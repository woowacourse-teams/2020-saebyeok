package com.saebyeok.saebyeok.documentation;

import com.saebyeok.saebyeok.documentation.common.Documentation;
import com.saebyeok.saebyeok.dto.EmotionDetailResponse;
import com.saebyeok.saebyeok.dto.EmotionResponse;
import com.saebyeok.saebyeok.dto.SubEmotionResponse;
import com.saebyeok.saebyeok.dto.TokenResponse;
import com.saebyeok.saebyeok.service.EmotionService;
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
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@WithUserDetails(userDetailsServiceBeanName = "userService", value = "a@a.com")
public class EmotionDocumentation extends Documentation {
    private static final Long EMOTION_ID = 1L;

    protected TokenResponse tokenResponse;
    private MockMvc mockMvc;

    @MockBean
    private EmotionService emotionService;

    @BeforeEach
    public void setUp(WebApplicationContext context, RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(documentationConfiguration(restDocumentation))
                .apply(springSecurity())
                .build();
        tokenResponse = new TokenResponse("token", "bearer");
    }

    @Test
    void readEmotion() throws Exception {
        List<SubEmotionResponse> subEmotionResponses = Arrays.asList(new SubEmotionResponse(1L, "행복해요"));
        EmotionDetailResponse emotionDetailResponse = new EmotionDetailResponse(1L, "좋아요", "리소스 링크", subEmotionResponses);

        given(emotionService.readEmotion(any())).willReturn(emotionDetailResponse);

        this.mockMvc.perform(get("/api/emotions/{emotionId}", EMOTION_ID).
                header("Authorization", tokenResponse.getTokenType() + " " + tokenResponse.getAccessToken()).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk()).
                andDo(print()).
                andDo(document("emotions/read",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer auth credentials")
                        ),
                        pathParameters(
                                parameterWithName("emotionId").description("조회할 감정 대분류의 ID")
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("조회할 감정 대분류의 ID"),
                                fieldWithPath("name").type(JsonFieldType.STRING).description("조회할 감정 대분류의 이름"),
                                fieldWithPath("imageResource").type(JsonFieldType.STRING).description("조회할 감정 대분류의 이미지 경로"),
                                fieldWithPath("subEmotions[]").type(JsonFieldType.ARRAY).description("조회할 감정 대분류에 속하는 감정 소분류 목록"),
                                fieldWithPath("subEmotions[].id").type(JsonFieldType.NUMBER).description("조회할 감정 대분류에 속하는 감정 소분류의 ID"),
                                fieldWithPath("subEmotions[].name").type(JsonFieldType.STRING).description("조회할 감정 대분류에 속하는 감정 소분류의 이름")
                        )
                ));
    }

    @Test
    void getEmotions() throws Exception {
        List<EmotionResponse> emotionResponses = Arrays.asList(new EmotionResponse(1L, "좋아요", "리소스 링크"));

        given(emotionService.getEmotions()).willReturn(emotionResponses);

        this.mockMvc.perform(get("/api/emotions").
                header("Authorization", tokenResponse.getTokenType() + " " + tokenResponse.getAccessToken()).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk()).
                andDo(print()).
                andDo(document("emotions/get",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer auth credentials")
                        ),
                        responseFields(
                                fieldWithPath("[]").type(JsonFieldType.ARRAY).description("조회할 감정 대분류의 목록"),
                                fieldWithPath("[].id").type(JsonFieldType.NUMBER).description("조회할 감정 대분류의 ID"),
                                fieldWithPath("[].name").type(JsonFieldType.STRING).description("조회할 감정 대분류의 이름"),
                                fieldWithPath("[].imageResource").type(JsonFieldType.STRING).description("조회할 감정 대분류의 이미지 경로")
                        )
                ));
    }
}
