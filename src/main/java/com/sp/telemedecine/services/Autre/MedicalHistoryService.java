package com.sp.telemedecine.services.Autre;

import com.sp.telemedecine.models.MedicalHistory;
import com.sp.telemedecine.repository.MedicalHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicalHistoryService {

    @Autowired
    private MedicalHistoryRepository medicalHistoryRepository;

    public List<MedicalHistory> getAllMedicalHistories() {
        return medicalHistoryRepository.findAll();
    }

    public MedicalHistory getMedicalHistoryById(Long id) {
        return medicalHistoryRepository.findById(id).orElse(null);
    }

    public MedicalHistory createMedicalHistory(MedicalHistory medicalHistory) {
        return medicalHistoryRepository.save(medicalHistory);
    }

    public MedicalHistory updateMedicalHistory(Long id, MedicalHistory medicalHistoryDetails) {
        MedicalHistory medicalHistory = medicalHistoryRepository.findById(id).orElse(null);
        if (medicalHistory != null) {
            medicalHistory.setDescription(medicalHistoryDetails.getDescription());
            medicalHistory.setDate(medicalHistoryDetails.getDate());
            return medicalHistoryRepository.save(medicalHistory);
        }
        return null;
    }

    public void deleteMedicalHistory(Long id) {
        medicalHistoryRepository.deleteById(id);
    }

    public List<MedicalHistory> getMedicalHistoriesByPatientId(Long patientId) {
        return medicalHistoryRepository.findByPatient_Id(patientId);
    }
}
