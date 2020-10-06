package com.saebyeok.saebyeok.domain;

import com.saebyeok.saebyeok.domain.report.ReportCategory;
import com.saebyeok.saebyeok.domain.report.ReportCategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import javax.transaction.Transactional;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@Sql("/truncate.sql")
@Transactional
@SpringBootTest
public class ReportCategoryRepositoryTest {

    @Autowired
    private ReportCategoryRepository reportCategoryRepository;

    private ReportCategory reportCategory1;
    private ReportCategory reportCategory2;
    private ReportCategory reportCategory3;

    @BeforeEach
    @Transactional
    void setUp() {
        reportCategoryRepository.deleteAll();
        reportCategory1 = new ReportCategory(1L, "분류1", "설명1");
        reportCategory2 = new ReportCategory(2L, "분류2", "설명2");
        reportCategory3 = new ReportCategory(3L, "분류3", "설명3");
        reportCategoryRepository.save(reportCategory1);
        reportCategoryRepository.save(reportCategory2);
        reportCategoryRepository.save(reportCategory3);
    }

    @DisplayName("전체 ReportCategory를 조회한다")
    @Test
    void findAllTest() {
        List<ReportCategory> reportCategories = reportCategoryRepository.findAll();
        assertThat(reportCategories).
                hasSize(3).
                extracting("name").
                containsOnly(reportCategory1.getName(), reportCategory2.getName(), reportCategory3.getName());
    }
}
