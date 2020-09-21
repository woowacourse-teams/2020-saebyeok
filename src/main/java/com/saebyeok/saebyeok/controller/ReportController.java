package com.saebyeok.saebyeok.controller;

import com.saebyeok.saebyeok.controller.resolver.LoginMember;
import com.saebyeok.saebyeok.domain.Member;
import com.saebyeok.saebyeok.dto.report.ArticleReportCreateRequest;
import com.saebyeok.saebyeok.dto.report.ArticleReportResponse;
import com.saebyeok.saebyeok.dto.report.ReportCategoryResponse;
import com.saebyeok.saebyeok.service.report.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Arrays;
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
        return ResponseEntity.
                created(URI.create("/reports/" + 1L)).
                build();
    }

    @GetMapping("/reports/article")
    public ResponseEntity<List<ArticleReportResponse>> getArticleReports(@LoginMember Member member) {
        List<ArticleReportResponse> reportResponses = Arrays.asList(new ArticleReportResponse(1L, "이 게시물을 신고합니다.", "", new ReportCategoryResponse(1L, "", ""), false));

        return ResponseEntity.ok(reportResponses);
    }

    @GetMapping("/reports/article/{reportId}")
    public ResponseEntity<ArticleReportResponse> readArticleReport(@LoginMember Member member, @PathVariable Long reportId) {
        ArticleReportResponse articleReportResponse = new ArticleReportResponse(1L, "이 게시물을 신고합니다.", "", new ReportCategoryResponse(1L, "", ""), false);

        return ResponseEntity.ok(articleReportResponse);
    }

    @DeleteMapping("/reports/article/{reportId}")
    public ResponseEntity<Void> deleteArticleReport(@LoginMember Member member, @PathVariable Long reportId) {
        return ResponseEntity.noContent().build();
    }
}
