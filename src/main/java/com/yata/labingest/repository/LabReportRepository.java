package com.yata.labingest.repository;

import com.yata.labingest.model.LabReport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LabReportRepository extends JpaRepository<LabReport, Long> {
    List<LabReport> findByEnterpriseCodeOrderByCreatedAtDesc(String enterpriseCode);
}
