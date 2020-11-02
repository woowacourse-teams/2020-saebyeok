package com.saebyeok.saebyeok.domain;

import com.saebyeok.saebyeok.exception.ArticleNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.auditing.AuditingHandler;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@Sql("/truncate.sql")
@Transactional
@SpringBootTest
class ArticleRepositoryTest {
    public static final int VISIBLE_DAYS_ON_FEED = 7;
    public static final int VISIBLE_DAYS_ON_ANALYSIS = 30;
    private static final PageRequest PAGE_REQUEST = PageRequest.of(0, 10);

    @MockBean
    private AuditingHandler auditingHandler;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private MemberRepository memberRepository;

    private Article article1;
    private Article article2;
    private Article article3;
    private Article article4;

    private Member member;

    @BeforeEach
    void setUp() {
        member = new Member(1L, "123456789", "naver", LocalDateTime.now(), false, Role.USER, new ArrayList<>());
        memberRepository.save(member);

        article1 = new Article(null, "내용1", member, LocalDateTime.now().minusDays(VISIBLE_DAYS_ON_ANALYSIS + 1),
                               false, false, new ArrayList<>());
        article2 = new Article(null, "내용2", member, LocalDateTime.now().minusDays(VISIBLE_DAYS_ON_FEED + 1), true,
                               false, new ArrayList<>());
        article3 = new Article(null, "내용3", member, LocalDateTime.now(), true, false, new ArrayList<>());
        article4 = new Article(null, "내용4", null, LocalDateTime.now(), true, false, new ArrayList<>());

        memberRepository.save(member);
        articleRepository.save(article1);
        articleRepository.save(article2);
        articleRepository.save(article3);
        articleRepository.save(article4);
    }

    @DisplayName("게시글 전체 조회를 한다")
    @Test
    void findAllByCreatedDateTest() {
        List<Article> articles = articleRepository.findAllByCreatedDateGreaterThanEqualAndIsDeleted(LocalDateTime.now().minusDays(VISIBLE_DAYS_ON_FEED), false, PAGE_REQUEST);

        assertThat(articles).
                hasSize(2).
                extracting("content").
                containsOnly(article3.getContent(), article4.getContent());
        assertThat(articles).
                extracting("isCommentAllowed").
                containsOnly(article3.getIsCommentAllowed(), article4.getIsCommentAllowed());
    }

    @DisplayName("게시글을 저장한다")
    @Test
    void saveTest() {
        Article newArticle = new Article("내용4", null, true);
        int articleSize = articleRepository.findAll().size();

        articleRepository.save(newArticle);

        List<Article> articlesAfterSave = articleRepository.findAll();
        assertThat(articlesAfterSave).hasSize(articleSize + 1).contains(newArticle);
    }

    @DisplayName("ID로 게시글을 조회하면 해당 게시글이 반환된다")
    @Test
    void findByIdAndCreatedDateTest() {
        Article savedArticle = articleRepository.findByIdAndCreatedDateGreaterThanEqualAndIsDeleted(article3.getId(), LocalDateTime.now().minusDays(VISIBLE_DAYS_ON_FEED), false)
                .orElseThrow(() -> new ArticleNotFoundException(article3.getId()));

        assertThat(savedArticle).isEqualTo(article3);
    }

    @DisplayName("ID로 게시글을 삭제요청하면 해당 게시글이 삭제된다")
    @Test
    void deleteByIdTest() {
        int articleSize = articleRepository.findAll().size();

        articleRepository.deleteById(article1.getId());

        List<Article> articlesAfterDelete = articleRepository.findAll();
        assertThat(articlesAfterDelete).hasSize(articleSize - 1).doesNotContain(article1);
    }

    @DisplayName("로그인한 사용자의 게시글 전체 조회를 한다")
    @Test
    void findAllByMemberTest() {
        List<Article> articles = articleRepository.findAllByMemberAndIsDeleted(member, false, PAGE_REQUEST);

        assertThat(articles).
                hasSize(3).
                extracting("content").
                containsOnly(article1.getContent(), article2.getContent(), article3.getContent());
        assertThat(articles).
                extracting("isCommentAllowed").
                containsOnly(article1.getIsCommentAllowed(), article2.getIsCommentAllowed(), article3.getIsCommentAllowed());
    }

    @DisplayName("ID와 Member 객체로 해당 사용자의 게시글을 조회하면 해당 게시글이 반환된다")
    @Test
    void findByIdAndMemberTest() {
        Article savedArticle = articleRepository.findByIdAndMemberEqualsAndIsDeleted(article2.getId(), member, false)
                .orElseThrow(() -> new ArticleNotFoundException(article2.getId()));

        assertThat(savedArticle).isEqualTo(article2);
    }

    @DisplayName("다른 사용자의 게시글을 조회할 수 없다")
    @Test
    void findByIdAndWrongMemberTest() {
        assertThat(articleRepository.findByIdAndMemberEqualsAndIsDeleted(article4.getId(), member, false)).isEmpty();
    }

    @DisplayName("7일이 넘은 글은 피드에 노출되지 않는다")
    @Test
    void excludeOldArticleForFeedTest() {
        List<Article> feedArticle = articleRepository.findAllByCreatedDateGreaterThanEqualAndIsDeleted(LocalDateTime.now().minusDays(VISIBLE_DAYS_ON_FEED), false);
        assertThat(feedArticle).containsExactly(article3, article4);
    }

    @DisplayName("30일이 넘은 글은 통계에 반영되지 않는다")
    @Test
    void excludeOldArticleForAnalysisTest() {
        List<Article> analysisArticle = articleRepository.findAllByMemberAndCreatedDateGreaterThanEqualAndIsDeleted(member, LocalDateTime.now().minusDays(VISIBLE_DAYS_ON_ANALYSIS), false);
        assertThat(analysisArticle).containsExactly(article2, article3);
    }
}