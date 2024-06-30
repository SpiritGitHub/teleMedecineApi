package com.sp.Medecine.services.Patient;

import com.sp.Medecine.models.Patient;
import com.sp.Medecine.repository.PatientRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PatientService {

    @Autowired
    private PatientRepo patientRepository;

    public Optional<Patient> findByUsername(String pseudo) {
        return patientRepository.findByUserPseudo(pseudo);
    }

    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    public Patient getPatientById(Long id) {
        return patientRepository.findById(id).orElse(null);
    }

    public Patient savePatient(Patient patient) {
        return patientRepository.save(patient);
    }

    public void deletePatient(Long id) {
        patientRepository.deleteById(id);
    }

    public Optional<Patient> getPatientByUserId(Long userId) {
        return patientRepository.findByUserUserId(userId);
    }
}