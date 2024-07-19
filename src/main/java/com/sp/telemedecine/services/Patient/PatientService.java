package com.sp.telemedecine.services.Patient;

import com.sp.telemedecine.models.Patient;
import com.sp.telemedecine.repository.PatientRepo;
import com.sp.telemedecine.services.Autre.GoogleCloudStorageService;
import io.jsonwebtoken.io.IOException;
import jakarta.persistence.EntityNotFoundException;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class PatientService {

    private final Logger logger = LoggerFactory.getLogger(PatientService.class);
    @Autowired
    private PatientRepo patientRepository;

    @Autowired
    private GoogleCloudStorageService googleCloudStorageService;

    public Optional<Patient> findByUsername(String pseudo) {
        return patientRepository.findByUserPseudo(pseudo);
    }

    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    public Patient getPatientById(Long patientId) {
        return patientRepository.findById(patientId).orElseThrow(() -> new IllegalArgumentException("Patient not found"));
    }

    @Transactional
    public ResponseEntity<Patient> updatePatient(Long userId, String lastName, String firstName, String gender, Date birthday, String address, String contactNumber, MultipartFile profilePicture) {
        try {
            logger.debug("Fetching patient with userId: {}", userId);
            Optional<Patient> optionalPatient = patientRepository.findByUserUserId(userId);
            if (!optionalPatient.isPresent()) {
                throw new EntityNotFoundException("Patient not found for userId: " + userId);
            }

            Patient patient = optionalPatient.get();
            if (profilePicture != null && !profilePicture.isEmpty()) {
                logger.debug("Uploading file for patient with userId: {}", userId);
                String imageUrl = googleCloudStorageService.uploadFile(profilePicture, "patient");
                if (imageUrl == null || imageUrl.trim().isEmpty()) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
                }
                patient.setProfilePictureUrl(imageUrl);
            }

            patient.setLastName(lastName);
            patient.setFirstName(firstName);
            patient.setGender(gender);
            patient.setBirthday(birthday);
            patient.setAddress(address);
            patient.setContactNumber(contactNumber);

            patientRepository.save(patient);
            logger.debug("Patient updated successfully with userId: {}", userId);
            return ResponseEntity.ok(patient);
        } catch (EntityNotFoundException e) {
            logger.error("Patient not found for userId: {}", userId, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (IOException e) {
            logger.error("Failed to upload file for patient with userId: {}", userId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        } catch (Exception e) {
            logger.error("Failed to update patient with userId: {}", userId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
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