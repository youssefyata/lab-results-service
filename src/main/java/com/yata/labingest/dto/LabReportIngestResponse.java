package com.yata.labingest.dto;

import lombok.Builder;
import lombok.Value;

import java.time.Instant;

@Value
@Builder
public class LabReportIngestResponse {
    Long id;
    Long reportId;
    String enterpriseCode;
    String patientId;
    String pdfUrl;
    Instant createdAt;
}
