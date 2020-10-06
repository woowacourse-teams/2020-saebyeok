package com.saebyeok.saebyeok.domain;

import com.saebyeok.saebyeok.domain.report.CommentReport;
import com.saebyeok.saebyeok.domain.report.CommentReportRepository;
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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@Sql("/truncate.sql")
@Transactional
@SpringBootTest
public class CommentReportRepositoryTest {
    @Autowired
    private CommentReportRepository commentReportRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ReportCategoryRepository reportCategoryRepository;

    private Comment comment;
    private Member member;
    private ReportCategory reportCategory;

    @BeforeEach
    @Transactional
    void setUp() {
        reportCategory = new ReportCategory(1L, "분류1", "설명1");
        reportCategoryRepository.save(reportCategory);
        comment = new Comment("HI", "nickName");
        commentRepository.save(comment);
        member = new Member(1L, "123456789", "naver", LocalDateTime.now(), false, Role.USER, new ArrayList<>());
        memberRepository.save(member);
    }

    @DisplayName("댓글 신고를 저장한다")
    @Test
    void saveTest() {
        CommentReport newCommentReport = new CommentReport("신고 내용4", member, comment, reportCategory);
        int commentReportSize = commentReportRepository.findAll().size();

        commentReportRepository.save(newCommentReport);

        List<CommentReport> commentReports = commentReportRepository.findAll();
        assertThat(commentReports).hasSize(commentReportSize + 1).contains(newCommentReport);
    }
}
