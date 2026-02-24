package com.yata.labingest.service;

import com.yata.labingest.dto.LabReportIngestRequest;
import com.yata.labingest.dto.LabReportIngestResponse;
import com.yata.labingest.model.LabReport;
import com.yata.labingest.model.LabResult;
import com.yata.labingest.model.Patient;
import com.yata.labingest.repository.LabReportRepository;
import com.yata.labingest.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LabReportIngestionService {

    private static final DateTimeFormatter OBJECT_DATE_FORMAT =
            DateTimeFormatter.ofPattern("yyyyMMddHHmmss").withZone(ZoneOffset.UTC);

    private final PatientRepository patientRepository;
    private final LabReportRepository labReportRepository;
    private final StorageService storageService;

    @Value("${SPACE_BUCKET}")
    private String spaceBucket;

    @Transactional
    public LabReportIngestResponse ingest(LabReportIngestRequest request) {
        Patient patient = upsertPatient(request.getPatient());
        byte[] pdfBytes = decodeBase64(request.getPdfBytesBase64());

        String objectName = buildObjectName(request.getReportId(), patient.getPatientExternalId());
        String storedPdfUrl = storageService.uploadPdf(
                spaceBucket,
                objectName,
                new ByteArrayInputStream(pdfBytes),
                pdfBytes.length
        );

        LabReport report = new LabReport();
        report.setReportExternalId(request.getReportId());
        report.setEnterpriseCode(request.getEnterpriseCode());
        report.setPatient(patient);
        report.setSourcePdfPath(request.getPdfPath());
        report.setSourcePdfUrl(request.getPdfUrl());
        report.setStoredPdfUrl(storedPdfUrl);
        report.setCreatedAt(Instant.now());

        request.getResults().forEach(item -> {
            LabResult result = new LabResult();
            result.setReport(report);
            result.setTestCode(item.getTestCode());
            result.setTestName(item.getTestName());
            result.setValue(item.getValue());
            result.setUnit(item.getUnit());
            result.setReferenceRange(item.getReferenceRange());
            result.setAbnormalFlag(item.getAbnormalFlag());
            result.setTestType(item.getTestType());
            report.getResults().add(result);
        });

        LabReport saved = labReportRepository.save(report);
        return mapToResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<LabReportIngestResponse> findByEnterpriseCode(String enterpriseCode) {
        return labReportRepository.findByEnterpriseCodeOrderByCreatedAtDesc(enterpriseCode).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private Patient upsertPatient(LabReportIngestRequest.PatientPayload payload) {
        Instant now = Instant.now();
        Patient patient = patientRepository.findByPatientExternalId(payload.getPatientId())
                .orElseGet(Patient::new);

        patient.setPatientExternalId(payload.getPatientId());
        patient.setLastName(payload.getLastName());
        patient.setFirstName(payload.getFirstName());
        patient.setBirthDate(payload.getBirthDate());
        patient.setSex(payload.getSex());
        patient.setUpdatedAt(now);
        if (patient.getCreatedAt() == null) {
            patient.setCreatedAt(now);
        }
        return patientRepository.save(patient);
    }

    private byte[] decodeBase64(String rawBase64) {
        String payload = rawBase64;
        if (payload.contains(",")) {
            payload = payload.substring(payload.indexOf(",") + 1);
        }
        try {
            return Base64.getDecoder().decode(payload);
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("pdf_bytes_base64 is invalid");
        }
    }

    private String buildObjectName(Long reportId, String patientId) {
        String datePart = OBJECT_DATE_FORMAT.format(Instant.now());
        return "lab-reports/" + patientId + "/resultat_" + reportId + "_" + datePart + "_" + UUID.randomUUID() + ".pdf";
    }

    private LabReportIngestResponse mapToResponse(LabReport report) {
        return LabReportIngestResponse.builder()
                .id(report.getId())
                .reportId(report.getReportExternalId())
                .enterpriseCode(report.getEnterpriseCode())
                .patientId(report.getPatient().getPatientExternalId())
                .pdfUrl(report.getStoredPdfUrl())
                .createdAt(report.getCreatedAt())
                .build();
    }
}
