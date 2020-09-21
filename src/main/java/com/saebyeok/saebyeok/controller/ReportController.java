package com.saebyeok.saebyeok.controller;

import com.saebyeok.saebyeok.controller.resolver.LoginMember;
import com.saebyeok.saebyeok.domain.Member;
import com.saebyeok.saebyeok.domain.report.ArticleReport;
import com.saebyeok.saebyeok.domain.report.CommentReport;
import com.saebyeok.saebyeok.dto.report.*;
import com.saebyeok.saebyeok.service.report.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class ReportController {
    private final ReportService reportService;

    @GetMapping("/reports/categories")
    public ResponseEntity<List<ReportCategoryResponse>> getReportCategories() {
        List<ReportCategoryResponse> reportCategoryResponses = reportService.getReportCategories();

        return ResponseEntity.ok(reportCategoryResponses);
    }

    @PostMapping("/reports/article")
    public ResponseEntity<Void> createArticleReport(@LoginMember Member member, @RequestBody ArticleReportCreateRequest request) {
        ArticleReport articleReport = reportService.createArticleReport(member, request);

        return ResponseEntity.
                created(URI.create("/reports/article" + articleReport.getId())).
                build();
    }

    @GetMapping("/reports/article")
    public ResponseEntity<List<ArticleReportResponse>> getArticleReports(@LoginMember Member member) {
        // TODO: 2020/09/21 admin 권한이 없다면 접근할 수 없도록 설정해야 함
        List<ArticleReportResponse> reportResponses = reportService.getArticleReports(member);

        return ResponseEntity.ok(reportResponses);
    }

    @GetMapping("/reports/article/{reportId}")
    public ResponseEntity<ArticleReportResponse> readArticleReport(@LoginMember Member member, @PathVariable Long reportId) {
        // TODO: 2020/09/21 admin 권한이 없다면 접근할 수 없도록 설정해야 함
        ArticleReportResponse articleReportResponse = reportService.readArticleReport(member, reportId);

        return ResponseEntity.ok(articleReportResponse);
    }

    @DeleteMapping("/reports/article/{reportId}")
    public ResponseEntity<Void> deleteArticleReport(@LoginMember Member member, @PathVariable Long reportId) {
        // TODO: 2020/09/21 admin 권한이 없다면 접근할 수 없도록 설정해야 함
        reportService.deleteArticleReport(member, reportId);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/reports/comment")
    public ResponseEntity<Void> createCommentReport(@LoginMember Member member, @RequestBody CommentReportCreateRequest request) {
        CommentReport commentReport = reportService.createCommentReport(member, request);

        return ResponseEntity.
                created(URI.create("/reports/comment" + commentReport.getId())).
                build();
    }

    @GetMapping("/reports/comment")
    public ResponseEntity<List<CommentReportResponse>> getCommentReports(@LoginMember Member member) {
        // TODO: 2020/09/21 admin 권한이 없다면 접근할 수 없도록 설정해야 함
        List<CommentReportResponse> reportResponses = reportService.getCommentReports(member);

        return ResponseEntity.ok(reportResponses);
    }

    @GetMapping("/reports/comment/{reportId}")
    public ResponseEntity<CommentReportResponse> readCommentReport(@LoginMember Member member, @PathVariable Long reportId) {
        // TODO: 2020/09/21 admin 권한이 없다면 접근할 수 없도록 설정해야 함
        CommentReportResponse commentReportResponse = reportService.readCommentReport(member, reportId);

        return ResponseEntity.ok(commentReportResponse);
    }

    @DeleteMapping("/reports/comment/{reportId}")
    public ResponseEntity<Void> deleteCommentReport(@LoginMember Member member, @PathVariable Long reportId) {
        // TODO: 2020/09/21 admin 권한이 없다면 접근할 수 없도록 설정해야 함
        reportService.deleteCommentReport(member, reportId);

        return ResponseEntity.noContent().build();
    }
}
