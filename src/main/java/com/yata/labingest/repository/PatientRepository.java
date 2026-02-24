package com.yata.labingest.repository;

import com.yata.labingest.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Long> {
    Optional<Patient> findByPatientExternalId(String patientExternalId);
}
