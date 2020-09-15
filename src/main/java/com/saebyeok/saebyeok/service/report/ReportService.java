package com.saebyeok.saebyeok.service.report;

import com.saebyeok.saebyeok.dto.report.ReportCategoryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ReportService {

    public List<ReportCategoryResponse> getReportCategories() {
        return Arrays.asList(
                new ReportCategoryResponse(1L, "이름1", "내용1"),
                new ReportCategoryResponse(2L, "이름1", "내용1"),
                new ReportCategoryResponse(3L, "이름1", "내용1")
        );
    }
}
