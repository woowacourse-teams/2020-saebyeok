package com.saebyeok.saebyeok.service.report;

import com.saebyeok.saebyeok.domain.Article;
import com.saebyeok.saebyeok.domain.Comment;
import com.saebyeok.saebyeok.domain.Member;
import com.saebyeok.saebyeok.domain.report.*;
import com.saebyeok.saebyeok.dto.ArticleResponse;
import com.saebyeok.saebyeok.dto.CommentResponse;
import com.saebyeok.saebyeok.dto.report.*;
import com.saebyeok.saebyeok.exception.ReportCategoryNotFoundException;
import com.saebyeok.saebyeok.exception.ReportNotFoundException;
import com.saebyeok.saebyeok.service.ArticleService;
import com.saebyeok.saebyeok.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ReportService {

    private final ArticleService articleService;
    private final CommentService commentService;
    private final ReportCategoryRepository reportCategoryRepository;
    private final ArticleReportRepository articleReportRepository;
    private final CommentReportRepository commentReportRepository;

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
        Article article = articleService.findArticleById(request.getArticleId());

        ArticleReport articleReport = request.toArticleReport(member, article, reportCategory);

        return articleReportRepository.save(articleReport);
    }

    public List<ArticleReportResponse> getArticleReports(Member member) {
        return articleReportRepository.findAll().
                stream().
                map(articleReport -> {
                    Long articleId = articleReport.getArticle().getId();
                    ArticleResponse articleResponse = articleService.readMemberArticle(member, articleId);
                    return new ArticleReportResponse(articleReport, articleResponse);
                }).
                collect(Collectors.toList());
    }

    public ArticleReportResponse readArticleReport(Member member, Long reportId) {
        ArticleReport articleReport = articleReportRepository.findById(reportId).
                orElseThrow(() -> new ReportNotFoundException(reportId));
        Long articleId = articleReport.getArticle().getId();
        ArticleResponse articleResponse = articleService.readMemberArticle(member, articleId);

        return new ArticleReportResponse(articleReport, articleResponse);
    }

    @Transactional
    public void deleteArticleReport(Member member, Long reportId) {
        ArticleReport articleReport = articleReportRepository.findById(reportId).
                orElseThrow(() -> new ReportNotFoundException(reportId));

        articleReport.finish();
    }

    @Transactional
    public CommentReport createCommentReport(Member member, CommentReportCreateRequest request) {
        ReportCategory reportCategory = reportCategoryRepository.findById(request.getReportCategoryId()).
                orElseThrow(() -> new ReportCategoryNotFoundException(request.getReportCategoryId()));
        Comment comment = commentService.findCommentById(request.getCommentId());

        CommentReport commentReport = request.toCommentReport(member, comment, reportCategory);

        return commentReportRepository.save(commentReport);
    }

    public List<CommentReportResponse> getCommentReports(Member member) {
        return commentReportRepository.findAll().
                stream().
                map(commentReport -> {
                    CommentResponse commentResponse = new CommentResponse(commentReport.getComment(), member);
                    return new CommentReportResponse(commentReport, commentResponse);
                }).
                collect(Collectors.toList());
    }

    public CommentReportResponse readCommentReport(Member member, Long reportId) {
        CommentReport commentReport = commentReportRepository.findById(reportId).
                orElseThrow(() -> new ReportNotFoundException(reportId));
        CommentResponse commentResponse = new CommentResponse(commentReport.getComment(), member);

        return new CommentReportResponse(commentReport, commentResponse);
    }

    @Transactional
    public void deleteCommentReport(Member member, Long reportId) {
        CommentReport commentReport = commentReportRepository.findById(reportId).
                orElseThrow(() -> new ReportNotFoundException(reportId));

        commentReport.finish();
    }
}
