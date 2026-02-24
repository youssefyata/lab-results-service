package com.yata.labingest.web;

import com.yata.labingest.dto.LabReportIngestRequest;
import com.yata.labingest.dto.LabReportIngestResponse;
import com.yata.labingest.service.LabReportIngestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/lab-reports")
public class LabReportIngestionController {

    private final LabReportIngestionService labReportIngestionService;

    @PostMapping("/ingest")
    public ResponseEntity<LabReportIngestResponse> ingest(@Valid @RequestBody LabReportIngestRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(labReportIngestionService.ingest(request));
    }

    @GetMapping
    public ResponseEntity<List<LabReportIngestResponse>> findByEnterpriseCode(
            @RequestParam("enterprise_code") @NotBlank String enterpriseCode
    ) {
        return ResponseEntity.ok(labReportIngestionService.findByEnterpriseCode(enterpriseCode));
    }
}
