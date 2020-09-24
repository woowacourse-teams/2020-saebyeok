package com.saebyeok.saebyeok.acceptance;

import com.saebyeok.saebyeok.dto.report.ReportCategoryResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ReportCategoryAcceptanceTest extends AcceptanceTest {
    /**
     * Scenario: ReportCategory를 조회할 수 있다.
     * <p>
     * given DB에 ReportCategory가 저장되어 있다.
     * when ReportCategory의 목록을 요청한다.
     * then ReportCategory의 목록을 반환한다.
     **/

    @DisplayName("신고 카테고리에 대해 요청을 보낼 때, 응답이 올바르게 수행되어야 한다.")
    @Test
    void manageReportCategory() {
        //when ReportCategory의 목록을 요청한다.
        List<ReportCategoryResponse> reportCategories = getReportCategories();

        //then ReportCategory의 목록을 반환한다.
        assertThat(reportCategories).
                hasSize(3).
                extracting("id").
                containsOnly(1L, 2L, 3L);
    }

    private List<ReportCategoryResponse> getReportCategories() {
        //@formatter:off
        return
                given().
                        auth().oauth2(TOKEN).
                when().
                        get(API + "/reports/categories").
                then().
                        log().all().
                        extract().
                        jsonPath().
                        getList(".", ReportCategoryResponse.class);
        //@formatter:on
    }
}
