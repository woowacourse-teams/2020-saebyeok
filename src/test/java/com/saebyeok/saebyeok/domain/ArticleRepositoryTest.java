package com.saebyeok.saebyeok.domain;

import com.saebyeok.saebyeok.exception.ArticleNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
    private static final LocalDateTime LIMIT_DATE = LocalDateTime.now().minusDays(7);
    private static final PageRequest PAGE_REQUEST = PageRequest.of(0, 10);

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private MemberRepository memberRepository;

    private Article article1;
    private Article article2;
    private Article article3;

    private Member member;

    @BeforeEach
    @Transactional
    void setUp() {
        // TODO: 2020/08/12  emotion,sql 실행시킨 다음에, null 대신 진짜 값 넣어주면 어떨까? 고민
        article1 = new Article("내용1", true);
        article2 = new Article("내용2", false);
        article3 = new Article("내용3", true);

        member = new Member(1L, "123456789", "naver", LocalDateTime.now(), false, Role.USER, new ArrayList<>());
        memberRepository.save(member);

        article2.setMember(member);

        articleRepository.save(article1);
        articleRepository.save(article2);
        articleRepository.save(article3);
    }

    @DisplayName("게시글 전체 조회를 한다")
    @Test
    void findAllByCreatedDateTest() {
        List<Article> articles = articleRepository.findAllByCreatedDateGreaterThanEqual(LIMIT_DATE, PAGE_REQUEST);

        assertThat(articles).
                hasSize(3).
                extracting("content").
                containsOnly(article1.getContent(), article2.getContent(), article3.getContent());
        assertThat(articles).
                extracting("isCommentAllowed").
                containsOnly(article1.getIsCommentAllowed(), article2.getIsCommentAllowed(), article3.getIsCommentAllowed());
    }

    @DisplayName("게시글을 저장한다")
    @Test
    void saveTest() {
        Article newArticle = new Article("내용4", true);
        int articleSize = articleRepository.findAll().size();

        articleRepository.save(newArticle);

        List<Article> articlesAfterSave = articleRepository.findAll();
        assertThat(articlesAfterSave).hasSize(articleSize + 1).contains(newArticle);
    }

    @DisplayName("ID로 게시글을 조회하면 해당 게시글이 반환된다")
    @Test
    void findByIdAndCreatedDateTest() {
        Article savedArticle = articleRepository.findByIdAndCreatedDateGreaterThanEqual(article1.getId(), LIMIT_DATE)
                .orElseThrow(() -> new ArticleNotFoundException(article1.getId()));

        assertThat(savedArticle).isEqualTo(article1);
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
        List<Article> articles = articleRepository.findAllByMember(member, PAGE_REQUEST);

        assertThat(articles).
                hasSize(1).
                extracting("content").
                containsOnly(article2.getContent());
        assertThat(articles).
                extracting("isCommentAllowed").
                containsOnly(article2.getIsCommentAllowed());
    }

    @DisplayName("ID와 Member 객체로 해당 사용자의 게시글을 조회하면 해당 게시글이 반환된다")
    @Test
    void findByIdAndMemberTest() {
        Article savedArticle = articleRepository.findByIdAndMemberEquals(article2.getId(), member)
                .orElseThrow(() -> new ArticleNotFoundException(article2.getId()));

        assertThat(savedArticle).isEqualTo(article2);
    }

    @DisplayName("다른 사용자의 게시글을 조회할 수 없다")
    @Test
    void findByIdAndWrongMemberTest() {
        assertThat(articleRepository.findByIdAndMemberEquals(article1.getId(), member)).isEmpty();
    }
}