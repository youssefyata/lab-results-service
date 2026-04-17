package com.yata.labingest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class SupabaseIngestPayload {

    @JsonProperty("code_fiche")
    String codeFiche;

    @JsonProperty("report_external_id")
    Long reportExternalId;

    @JsonProperty("source_system")
    String sourceSystem;

    List<ResultItem> results;

    @Value
    @Builder
    public static class ResultItem {
        @JsonProperty("test_code")
        String testCode;

        @JsonProperty("test_name")
        String testName;

        @JsonProperty("result_value")
        String resultValue;

        String unit;

        @JsonProperty("reference_range")
        String referenceRange;

        @JsonProperty("abnormal_flag")
        String abnormalFlag;

        @JsonProperty("test_type")
        String testType;

        @JsonProperty("report_id")
        Long reportId;
    }
}
