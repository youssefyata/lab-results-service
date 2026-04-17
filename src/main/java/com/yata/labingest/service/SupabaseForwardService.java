package com.yata.labingest.service;

import com.yata.labingest.dto.LabReportIngestRequest;
import com.yata.labingest.dto.SupabaseIngestPayload;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.stream.Collectors;

@Slf4j
@Service
public class SupabaseForwardService {

    private final RestTemplate restTemplate;
    private final String supabaseUrl;
    private final String apiKey;

    public SupabaseForwardService(
            RestTemplate restTemplate,
            @Value("${supabase.ingest.url}") String supabaseUrl,
            @Value("${supabase.ingest.api-key}") String apiKey) {
        this.restTemplate = restTemplate;
        this.supabaseUrl = supabaseUrl;
        this.apiKey = apiKey;
    }

    @Async
    public void forward(LabReportIngestRequest request) {
        SupabaseIngestPayload payload = mapToPayload(request);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-api-key", apiKey);

        HttpEntity<SupabaseIngestPayload> entity = new HttpEntity<>(payload, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(supabaseUrl, entity, String.class);
            log.info("Supabase forward OK for code_fiche={} — status={}, body={}",
                    payload.getCodeFiche(), response.getStatusCodeValue(), response.getBody());
        } catch (Exception ex) {
            log.error("Supabase forward FAILED for code_fiche={}: {}", payload.getCodeFiche(), ex.getMessage());
        }
    }

    private SupabaseIngestPayload mapToPayload(LabReportIngestRequest request) {
        return SupabaseIngestPayload.builder()
                .codeFiche(request.getPatient().getPatientId())
                .reportExternalId(request.getReportId())
                .sourceSystem(null)
                .results(request.getResults().stream()
                        .map(r -> SupabaseIngestPayload.ResultItem.builder()
                                .testCode(r.getTestCode())
                                .testName(r.getTestName())
                                .resultValue(r.getValue())
                                .unit(r.getUnit())
                                .referenceRange(r.getReferenceRange())
                                .abnormalFlag(r.getAbnormalFlag())
                                .testType(r.getTestType())
                                .reportId(request.getReportId())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }
}
