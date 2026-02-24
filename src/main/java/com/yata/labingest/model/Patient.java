package com.yata.labingest.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "patients")
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "patient_external_id", nullable = false, unique = true, length = 64)
    private String patientExternalId;

    @Column(name = "last_name", length = 128)
    private String lastName;

    @Column(name = "first_name", length = 128)
    private String firstName;

    @Column(name = "birth_date", length = 16)
    private String birthDate;

    @Column(name = "sex", length = 8)
    private String sex;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;
}
