package com.saebyeok.saebyeok.dto.report;

import com.saebyeok.saebyeok.domain.Comment;
import com.saebyeok.saebyeok.domain.Member;
import com.saebyeok.saebyeok.domain.report.CommentReport;
import com.saebyeok.saebyeok.domain.report.ReportCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentReportCreateRequest {
    private String content;

    @NotNull(message = "신고할 댓글을 반드시 선택해 주셔야 해요.")
    private Long commentId;
    @NotNull(message = "신고 분류는 반드시 선택되어야 해요.")
    private Long reportCategoryId;

    public CommentReport toCommentReport(Member member, Comment comment, ReportCategory reportCategory) {
        return new CommentReport(content, member, comment, reportCategory);
    }
}
