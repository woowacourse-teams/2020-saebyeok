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

        member = new Member(1L, "a@a.com", 1991, Gender.FEMALE, LocalDateTime.now(),
                            false, Role.USER, new ArrayList<>());
        memberRepository.save(member);

        article1.setMember(member);
        article2.setMember(member);
        article3.setMember(member);

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

    @DisplayName("Member의 ID로 게시글을 조회하면 해당 게시글의 ID가 반환된다")
    @Test
    void findArticlesByMemberIdTest() {
        List<Long> memberArticlesIds = articleRepository.findArticlesByMemberId(member.getId());

        assertThat(memberArticlesIds.size()).isEqualTo(3);
        assertThat(memberArticlesIds.get(0)).isEqualTo(1L);
        assertThat(memberArticlesIds.get(1)).isEqualTo(2L);
        assertThat(memberArticlesIds.get(2)).isEqualTo(3L);
    }

    @DisplayName("글을 한 개도 작성하지 않은 Member의 ID로 게시글을 조회하면 빈 리스트가 반환된다")
    @Test
    void findNoArticleByMemberIdTest() {
        Member tempMember = new Member(2L, "b@b.com", 1991, Gender.FEMALE, LocalDateTime.now(),
                                       false, Role.USER, new ArrayList<>());
        memberRepository.save(tempMember);

        List<Long> memberArticlesIds = articleRepository.findArticlesByMemberId(tempMember.getId());

        assertThat(memberArticlesIds.size()).isZero();
    }
}