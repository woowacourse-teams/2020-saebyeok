package com.saebyeok.saebyeok.controller;

import com.saebyeok.saebyeok.controller.resolver.LoginMember;
import com.saebyeok.saebyeok.domain.Member;
import com.saebyeok.saebyeok.domain.report.ArticleReport;
import com.saebyeok.saebyeok.domain.report.CommentReport;
import com.saebyeok.saebyeok.dto.report.ArticleReportCreateRequest;
import com.saebyeok.saebyeok.dto.report.CommentReportCreateRequest;
import com.saebyeok.saebyeok.dto.report.ReportCategoryResponse;
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
    // TODO: 2020/10/05 현재 조회 로직 없이 생성 로직만 있어서, 부득이하게 인수테스트를 만들지 않았음. 추후 작성핟것.
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

    @PostMapping("/reports/comment")
    public ResponseEntity<Void> createCommentReport(@LoginMember Member member, @RequestBody CommentReportCreateRequest request) {
        CommentReport commentReport = reportService.createCommentReport(member, request);

        return ResponseEntity.
                created(URI.create("/reports/comment" + commentReport.getId())).
                build();
    }
}
