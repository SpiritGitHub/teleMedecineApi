package com.sp.telemedecine.services.Patient;

import com.sp.telemedecine.models.Patient;
import com.sp.telemedecine.repository.PatientRepo;
import jakarta.persistence.EntityNotFoundException;
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

    public Patient getPatientById(Long patientId) {
        return patientRepository.findById(patientId).orElseThrow(() -> new IllegalArgumentException("Patient not found"));
    }

    public Patient updatePatient(Long id, Patient patientDetails) {
        Optional<Patient> optionalPatient = patientRepository.findById(id);
        if (optionalPatient.isPresent()) {
            Patient patient = optionalPatient.get();
            patient.setLastName(patientDetails.getLastName());
            patient.setFirstName(patientDetails.getFirstName());
            patient.setGender(patientDetails.getGender());
            patient.setBirthday(patientDetails.getBirthday());
            patient.setAddress(patientDetails.getAddress());
            patient.setContactNumber(patientDetails.getContactNumber());
            patient.setProfilePictureUrl(patientDetails.getProfilePictureUrl());
            return patientRepository.save(patient);
        } else {
            throw new EntityNotFoundException("Patient not found for id: " + id);
        }
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