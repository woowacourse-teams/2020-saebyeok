package com.saebyeok.saebyeok.dto.report;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ArticleReportCreateRequest {
    private String content;

    @NotNull(message = "신고할 게시물을 반드시 선택해 주셔야 해요.")
    private Long articleId;
    @NotNull(message = "신고 분류는 반드시 선택되어야 해요.")
    private Long reportCategoryId;
}
