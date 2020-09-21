package com.saebyeok.saebyeok.service;

import com.saebyeok.saebyeok.domain.*;
import com.saebyeok.saebyeok.dto.RecommentCreateRequest;
import com.saebyeok.saebyeok.util.NicknameGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Sql("/truncate.sql")
@ExtendWith(MockitoExtension.class)
class RecommentServiceTest {

    private static final String RECOMMENT_CONTENT = "대댓글 내용입니다.";
    private static final Long ARTICLE_ID = 1L;
    private static final Long COMMENT_ID = 1L;

    private RecommentService recommentService;
    private Member member;

    @Mock
    private RecommentRepository recommentRepository;
    @Mock
    private ArticleRepository articleRepository;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private NicknameGenerator nicknameGenerator;

    @BeforeEach
    void setUp() {
        this.recommentService = new RecommentService(recommentRepository, articleRepository, commentRepository, nicknameGenerator);
        this.member = new Member();
    }

    @DisplayName("대댓글 등록 메서드를 호출했을 때, 대댓글 등록을 수행한다.")
    @Test
    void createRecommentTest() {
        when(articleRepository.findById(ARTICLE_ID)).thenReturn(Optional.of(new Article()));
        when(commentRepository.findById(COMMENT_ID)).thenReturn(Optional.of(new Comment()));

        RecommentCreateRequest request = new RecommentCreateRequest(RECOMMENT_CONTENT, ARTICLE_ID, COMMENT_ID);
        recommentService.createRecomment(member, request);

        verify(recommentRepository).save(any());
    }

    @DisplayName("예외 테스트: 존재하지 않는 게시글에 대댓글 작성 요청을 하면 예외가 발생해야 한다.")
    @Test
    void invalidRequestTest1() {
    }

    @DisplayName("예외 테스트: 존재하지 않는 댓글에 대댓글 작성 요청을 하면 예외가 발생해야 한다.")
    @Test
    void invalidRequestTest2() {
    }
}