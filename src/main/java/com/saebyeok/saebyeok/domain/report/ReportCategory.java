package com.saebyeok.saebyeok.domain.report;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ReportCategory {

    @Id
    @Column(name = "REPORT_CATEGORY_ID")
    private Long id;

    private String name;
    private String content;
}
