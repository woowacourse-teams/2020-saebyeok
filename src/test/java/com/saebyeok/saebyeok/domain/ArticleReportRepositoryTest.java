package com.saebyeok.saebyeok.domain;

import com.saebyeok.saebyeok.domain.report.ArticleReport;
import com.saebyeok.saebyeok.domain.report.ArticleReportRepository;
import com.saebyeok.saebyeok.domain.report.ReportCategory;
import com.saebyeok.saebyeok.domain.report.ReportCategoryRepository;
import com.saebyeok.saebyeok.exception.ReportNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
public class ArticleReportRepositoryTest {

    @Autowired
    private ArticleReportRepository articleReportRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private ReportCategoryRepository reportCategoryRepository;

    private Article article;
    private Member member;
    private ReportCategory reportCategory;

    private ArticleReport articleReport1;
    private ArticleReport articleReport2;
    private ArticleReport articleReport3;

    @BeforeEach
    @Transactional
    void setUp() {
        reportCategory = new ReportCategory(1L, "분류1", "설명1");
        reportCategoryRepository.save(reportCategory);
        article = new Article("내용1", true);
        articleRepository.save(article);
        member = new Member(1L, "123456789", "naver", LocalDateTime.now(), false, Role.USER, new ArrayList<>());
        memberRepository.save(member);

        articleReport1 = new ArticleReport("신고 내용1", member, article, reportCategory);
        articleReport2 = new ArticleReport("신고 내용2", member, article, reportCategory);
        articleReport3 = new ArticleReport("신고 내용3", member, article, reportCategory);
        articleReportRepository.save(articleReport1);
        articleReportRepository.save(articleReport2);
        articleReportRepository.save(articleReport3);
    }

    @DisplayName("게시글 신고를 저장한다")
    @Test
    void saveTest() {
        ArticleReport newArticleReport = new ArticleReport("신교 내용4", member, article, reportCategory);
        int articleReportSize = articleReportRepository.findAll().size();

        articleReportRepository.save(newArticleReport);

        List<ArticleReport> articleReports = articleReportRepository.findAll();
        assertThat(articleReports).hasSize(articleReportSize + 1).contains(newArticleReport);
    }

    @DisplayName("전체 ArticleReport를 조회한다")
    @Test
    void findAllTest() {
        List<ArticleReport> articleReports = articleReportRepository.findAll();
        assertThat(articleReports).
                hasSize(3).
                extracting("content").
                containsOnly(articleReport1.getContent(), articleReport2.getContent(), articleReport3.getContent());
    }

    @DisplayName("ID로 개별 ArticleReport를 조회한다")
    @Test
    void findByIdTest() {
        Long id = articleReport1.getId();
        ArticleReport articleReport = articleReportRepository.findById(id).
                orElseThrow(() -> new ReportNotFoundException(id));

        assertThat(articleReport).isNotNull();
        assertThat(articleReport.getId()).isEqualTo(id);
        assertThat(articleReport.getContent()).isEqualTo(articleReport1.getContent());
        assertThat(articleReport.getIsFinished()).isEqualTo(articleReport1.getIsFinished());
    }
}
