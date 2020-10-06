package com.saebyeok.saebyeok.service.report;

import com.saebyeok.saebyeok.domain.Article;
import com.saebyeok.saebyeok.domain.Comment;
import com.saebyeok.saebyeok.domain.Member;
import com.saebyeok.saebyeok.domain.report.*;
import com.saebyeok.saebyeok.dto.report.ArticleReportCreateRequest;
import com.saebyeok.saebyeok.dto.report.CommentReportCreateRequest;
import com.saebyeok.saebyeok.dto.report.ReportCategoryResponse;
import com.saebyeok.saebyeok.exception.ReportCategoryNotFoundException;
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
        ReportCategory reportCategory = readReportCategory(request.getReportCategoryId());
        Article article = articleService.findArticleById(request.getArticleId());

        ArticleReport articleReport = request.toArticleReport(member, article, reportCategory);

        return articleReportRepository.save(articleReport);
    }

    @Transactional
    public CommentReport createCommentReport(Member member, CommentReportCreateRequest request) {
        ReportCategory reportCategory = readReportCategory(request.getReportCategoryId());
        Comment comment = commentService.findCommentById(request.getCommentId());

        CommentReport commentReport = request.toCommentReport(member, comment, reportCategory);

        return commentReportRepository.save(commentReport);
    }

    private ReportCategory readReportCategory(Long reportCategoryId) {
        return reportCategoryRepository.findById(reportCategoryId).
                orElseThrow(() -> new ReportCategoryNotFoundException(reportCategoryId));
    }
}
