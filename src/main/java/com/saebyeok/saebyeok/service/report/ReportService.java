package com.saebyeok.saebyeok.service.report;

import com.saebyeok.saebyeok.domain.Article;
import com.saebyeok.saebyeok.domain.ArticleRepository;
import com.saebyeok.saebyeok.domain.Member;
import com.saebyeok.saebyeok.domain.report.ArticleReport;
import com.saebyeok.saebyeok.domain.report.ArticleReportRepository;
import com.saebyeok.saebyeok.domain.report.ReportCategory;
import com.saebyeok.saebyeok.domain.report.ReportCategoryRepository;
import com.saebyeok.saebyeok.dto.report.ArticleReportCreateRequest;
import com.saebyeok.saebyeok.dto.report.ArticleReportResponse;
import com.saebyeok.saebyeok.dto.report.ReportCategoryResponse;
import com.saebyeok.saebyeok.exception.ArticleNotFoundException;
import com.saebyeok.saebyeok.exception.ReportCategoryNotFoundException;
import com.saebyeok.saebyeok.exception.ReportNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ReportService {

    private final ArticleRepository articleRepository;
    private final ReportCategoryRepository reportCategoryRepository;
    private final ArticleReportRepository articleReportRepository;

    public List<ReportCategoryResponse> getReportCategories() {
        return reportCategoryRepository.findAll().
                stream().
                map(ReportCategoryResponse::new).
                collect(Collectors.toList());
    }

    @Transactional
    public ArticleReport createArticleReport(Member member, ArticleReportCreateRequest request) {
        ReportCategory reportCategory = reportCategoryRepository.findById(request.getReportCategoryId()).
                orElseThrow(() -> new ReportCategoryNotFoundException(request.getReportCategoryId()));
        Article article = articleRepository.findById(request.getArticleId()).
                orElseThrow(() -> new ArticleNotFoundException(request.getArticleId()));

        ArticleReport articleReport = request.toArticleReport(member, article, reportCategory);

        return articleReportRepository.save(articleReport);
    }

    public List<ArticleReportResponse> getArticleReports(Member member) {
        return articleReportRepository.findAll().
                stream().
                map(ArticleReportResponse::new).
                collect(Collectors.toList());
    }

    public ArticleReportResponse readArticleReport(Member member, Long reportId) {
        ArticleReport articleReport = articleReportRepository.findById(reportId).
                orElseThrow(() -> new ReportNotFoundException(reportId));
        return new ArticleReportResponse(articleReport);
    }

    @Transactional
    public void deleteArticleReport(Member member, Long reportId) {
        ArticleReport articleReport = articleReportRepository.findById(reportId).
                orElseThrow(() -> new ReportNotFoundException(reportId));

        articleReport.finish();
    }
}
