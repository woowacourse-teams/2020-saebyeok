package com.saebyeok.saebyeok.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
@ActiveProfiles("test")
@Sql("/truncate.sql")
@SpringBootTest
class ArticleLikeRepositoryTest {

    @Autowired
    private ArticleLikeRepository articleLikeRepository;
    private Member member;
    private Article article;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @BeforeEach
    void setUp() {
        this.member = new Member(1L, "123456789", "naver", LocalDateTime.now(), false, Role.USER, Collections.emptyList());
        this.article = new Article(1L, "내용", member, LocalDateTime.now(), false, Collections.emptyList(), 0L, Collections.emptyList());

        memberRepository.save(member);
        articleRepository.save(article);
    }

    @DisplayName("게시물에 공감을 등록할 수 있다")
    @Test
    void saveArticleLikeTest() {
        ArticleLike like = new ArticleLike(member, article);

        ArticleLike savedLike = articleLikeRepository.save(like);

        assertThat(savedLike).isNotNull();
        assertThat(savedLike).isEqualTo(like);
        assertThat(savedLike.getId()).isNotNull();
    }

    @DisplayName("예외 테스트: Member와 Article 참조가 없이 공감 등록을 하면 예외가 발생한다")
    @Test
    void saveArticleLikeWithoutMemberAndArticleTest() {
        ArticleLike like = new ArticleLike();

        assertThatThrownBy(() -> articleLikeRepository.save(like))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @DisplayName("예외 테스트: 참조할 수 없는 Member 혹은 Article 로 공감 등록을 하면 예외가 발생한다")
    @Test
    void saveArticleLikeWithInvalidMemberOrArticleTest() {
        ArticleLike likeWithInvalidMember = new ArticleLike(new Member(), article);
        ArticleLike likeWithInvalidArticle = new ArticleLike(member, new Article());

        assertThatThrownBy(() -> articleLikeRepository.save(likeWithInvalidMember))
                .isInstanceOf(InvalidDataAccessApiUsageException.class);

        assertThatThrownBy(() -> articleLikeRepository.save(likeWithInvalidArticle))
                .isInstanceOf(InvalidDataAccessApiUsageException.class);
    }

    @DisplayName("예외 테스트: 이미 공감한 게시물에 다시 공감을 요청하면 예외가 발생한다")
    @Test
    void saveArticleLikeWithExistingArticleLikeTest() {
        ArticleLike like = new ArticleLike(member, article);
        ArticleLike likeAgain = new ArticleLike(member, article);
        articleLikeRepository.save(like);

        assertThatThrownBy(() -> articleLikeRepository.save(likeAgain))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @DisplayName("게시글 공감을 취소할 수 있다")
    @Test
    void deleteArticleLikeTest() {
        ArticleLike like = new ArticleLike(member, article);
        articleLikeRepository.save(like);
        List<ArticleLike> beforeDelete = articleLikeRepository.findAll();

        articleLikeRepository.deleteByMemberAndArticle(member, article);

        List<ArticleLike> afterDelete = articleLikeRepository.findAll();

        assertThat(afterDelete.size()).isEqualTo(beforeDelete.size() - 1);
    }
}