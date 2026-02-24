package com.yata.labingest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class LabReportIngestRequest {

    @Valid
    @NotNull
    private PatientPayload patient;

    @Valid
    @NotEmpty
    private List<ResultPayload> results;

    @JsonProperty("pdf_path")
    private String pdfPath;

    @JsonProperty("pdf_url")
    private String pdfUrl;

    @JsonProperty("report_id")
    @NotNull
    private Long reportId;

    @JsonProperty("enterprise_code")
    @NotBlank
    private String enterpriseCode;

    @JsonProperty("pdf_bytes_base64")
    @NotBlank
    private String pdfBytesBase64;

    @Data
    public static class PatientPayload {
        @JsonProperty("patient_id")
        @NotBlank
        private String patientId;

        @JsonProperty("last_name")
        private String lastName;

        @JsonProperty("first_name")
        private String firstName;

        @JsonProperty("birth_date")
        private String birthDate;

        private String sex;
    }

    @Data
    public static class ResultPayload {
        @JsonProperty("test_code")
        private String testCode;

        @JsonProperty("test_name")
        private String testName;

        private String value;
        private String unit;

        @JsonProperty("reference_range")
        private String referenceRange;

        @JsonProperty("abnormal_flag")
        private String abnormalFlag;

        @JsonProperty("test_type")
        private String testType;
    }
}
