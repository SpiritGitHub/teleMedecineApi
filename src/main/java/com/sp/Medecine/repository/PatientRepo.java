package com.sp.Medecine.repository;

import com.sp.Medecine.models.Doctor;
import com.sp.Medecine.models.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PatientRepo extends JpaRepository <Patient, Long> {
    Optional<Patient> findByUserPseudo(String pseudo);
    Optional<Patient> findByUserUserId(Long userId);
}
