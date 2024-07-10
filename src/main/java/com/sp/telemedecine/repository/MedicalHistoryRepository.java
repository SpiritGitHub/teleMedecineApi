package com.sp.telemedecine.repository;

import com.sp.telemedecine.models.MedicalHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicalHistoryRepository extends JpaRepository<MedicalHistory, Long> {
    List<MedicalHistory> findByPatient_Id(Long patientId);
}
