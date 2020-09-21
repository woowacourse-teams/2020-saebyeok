package com.saebyeok.saebyeok.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.saebyeok.saebyeok.domain.Member;
import com.saebyeok.saebyeok.domain.Recomment;
import com.saebyeok.saebyeok.dto.RecommentCreateRequest;
import com.saebyeok.saebyeok.service.RecommentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithUserDetails(userDetailsServiceBeanName = "userService", value = "123456789")
@SpringBootTest
class RecommentControllerTest {
    private static final String API = "/api";
    private static final Long COMMENT_ID = 1L;
    private static final Long ARTICLE_ID = 1L;
    private static final Long RECOMMENT_ID = 1L;
    private static final String RECOMMENT_CONTENT = "대댓글 내용입니다.";

    private MockMvc mockMvc;

    @MockBean
    private RecommentService recommentService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp(WebApplicationContext context) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @DisplayName("대댓글 생성 요청시, 생성된 대댓글 id를 반환한다.")
    @Test
    void createRecommentTest() throws Exception {
        RecommentCreateRequest request = new RecommentCreateRequest(RECOMMENT_CONTENT, ARTICLE_ID, COMMENT_ID);
        String requestAsString = objectMapper.writeValueAsString(request);

        when(recommentService.createRecomment(any(Member.class), any())).thenReturn(new Recomment(RECOMMENT_ID, null, null, null, null, null, null, null, null));

        MvcResult mvcResult = this.mockMvc.perform(post(API + "/recomments")
                .content(requestAsString)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(print())
                .andReturn();

        Long savedRecommentId = Long.parseLong(mvcResult.getResponse().getContentAsString());

        assertThat(savedRecommentId).isEqualTo(RECOMMENT_ID);
    }
}