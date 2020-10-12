package com.saebyeok.saebyeok.service.report;

import com.saebyeok.saebyeok.domain.Member;
import com.saebyeok.saebyeok.domain.report.Report;
import com.saebyeok.saebyeok.domain.report.ReportCategory;
import com.saebyeok.saebyeok.domain.report.ReportCategoryRepository;
import com.saebyeok.saebyeok.domain.report.ReportRepository;
import com.saebyeok.saebyeok.dto.report.ReportCategoryResponse;
import com.saebyeok.saebyeok.dto.report.ReportCreateRequest;
import com.saebyeok.saebyeok.exception.ReportCategoryNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ReportService {
    private final ReportCategoryRepository reportCategoryRepository;
    private final ReportRepository reportRepository;

    public List<ReportCategoryResponse> getReportCategories() {
        return reportCategoryRepository.findAll().
                stream().
                map(ReportCategoryResponse::new).
                collect(Collectors.toList());
    }

    @Transactional
    public Report createReport(Member member, ReportCreateRequest request) {
        ReportCategory reportCategory = readReportCategory(request.getReportCategoryId());
        Report report = request.toReport(member, reportCategory);

        return reportRepository.save(report);
    }

    private ReportCategory readReportCategory(Long reportCategoryId) {
        return reportCategoryRepository.findById(reportCategoryId).
                orElseThrow(() -> new ReportCategoryNotFoundException(reportCategoryId));
    }
}
