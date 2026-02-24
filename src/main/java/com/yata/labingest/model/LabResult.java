package com.yata.labingest.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "lab_results")
public class LabResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "report_id", nullable = false)
    private LabReport report;

    @Column(name = "test_code", length = 64)
    private String testCode;

    @Column(name = "test_name", length = 256)
    private String testName;

    @Column(name = "result_value", length = 256)
    private String value;

    @Column(name = "unit", length = 64)
    private String unit;

    @Column(name = "reference_range", length = 128)
    private String referenceRange;

    @Column(name = "abnormal_flag", length = 8)
    private String abnormalFlag;

    @Column(name = "test_type", length = 128)
    private String testType;
}
