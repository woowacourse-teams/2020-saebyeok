package com.saebyeok.saebyeok.service.report;

import com.saebyeok.saebyeok.domain.report.ReportCategoryRepository;
import com.saebyeok.saebyeok.dto.report.ReportCategoryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ReportService {

    private final ReportCategoryRepository reportCategoryRepository;

    public List<ReportCategoryResponse> getReportCategories() {
        return reportCategoryRepository.findAll().
                stream().
                map(ReportCategoryResponse::new).
                collect(Collectors.toList());
    }
}
