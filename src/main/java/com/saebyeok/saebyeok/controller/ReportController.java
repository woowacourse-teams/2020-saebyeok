package com.saebyeok.saebyeok.controller;

import com.saebyeok.saebyeok.controller.resolver.LoginMember;
import com.saebyeok.saebyeok.domain.Member;
import com.saebyeok.saebyeok.domain.report.Report;
import com.saebyeok.saebyeok.dto.report.ReportCategoryResponse;
import com.saebyeok.saebyeok.dto.report.ReportCreateRequest;
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

    @PostMapping("/reports")
    public ResponseEntity<Void> createReport(@LoginMember Member member, @RequestBody ReportCreateRequest request) {
        Report report = reportService.createReport(member, request);

        return ResponseEntity.
                created(URI.create("/reports/" + report.getId())).
                build();
    }
}
