package com.saebyeok.saebyeok.controller;

import com.saebyeok.saebyeok.dto.report.ReportCategoryResponse;
import com.saebyeok.saebyeok.service.report.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class ReportController {
    private final ReportService reportService;

    @GetMapping("/report/categories")
    public ResponseEntity<List<ReportCategoryResponse>> getReportCategories() {
        List<ReportCategoryResponse> reportCategoryResponses = reportService.getReportCategories();
        
        return ResponseEntity.ok(reportCategoryResponses);
    }
}
