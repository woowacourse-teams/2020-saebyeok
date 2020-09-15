package com.saebyeok.saebyeok.controller;

import com.saebyeok.saebyeok.dto.report.ReportCategoryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class ReportController {
    //service

    @GetMapping("/report/categories")
    public ResponseEntity<List<ReportCategoryResponse>> getReportCategories() {
        List<ReportCategoryResponse> reportCategoryResponses = Arrays.asList(
                new ReportCategoryResponse(1L, "이름1", "내용1"),
                new ReportCategoryResponse(2L, "이름1", "내용1"),
                new ReportCategoryResponse(3L, "이름1", "내용1")
        );

        return ResponseEntity.ok(reportCategoryResponses);
    }
}
