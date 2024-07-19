package com.sp.telemedecine.services.doctor;

import com.sp.telemedecine.models.Doctor;
import com.sp.telemedecine.repository.DoctorRepo;
import com.sp.telemedecine.services.Autre.GoogleCloudStorageService;
import io.jsonwebtoken.io.IOException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
public class DoctorService {

    private final Logger logger = LoggerFactory.getLogger(DoctorService.class);

    @Autowired
    private DoctorRepo doctorRepository;
    @Autowired
    private GoogleCloudStorageService googleCloudStorageService;
    @Autowired
    private EntityManager entityManager;


    @Transactional
    public ResponseEntity<Doctor> updateDoctor(Long userId, String specialization, String gender, String address, String contactNumber, MultipartFile profilePicture) {
        try {
            Optional<Doctor> optionalDoctor = doctorRepository.findByUserUserId(userId);
            if (!optionalDoctor.isPresent()) {
                throw new EntityNotFoundException("Doctor not found for userId: " + userId);
            }

            Doctor doctor = optionalDoctor.get();
            if (profilePicture != null && !profilePicture.isEmpty()) {
                try {
                    String imageUrl = googleCloudStorageService.uploadFile(profilePicture, "doctor");
                    logger.debug("Uploaded profile picture URL: {}", imageUrl);
                    if (imageUrl == null || imageUrl.trim().isEmpty()) {
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
                    }
                    doctor.setProfilePictureUrl(imageUrl);
                } catch (IOException e) {
                    logger.error("Error uploading file for userId: {}", userId, e);
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
                }
            }

            doctor.setSpecialization(specialization);
            doctor.setGender(gender);
            doctor.setAddress(address);
            doctor.setContactNumber(contactNumber);

            try {
                doctorRepository.save(doctor);
                logger.debug("Doctor updated successfully: {}", doctor);
            } catch (Exception e) {
                logger.error("Error saving doctor for userId: {}", userId, e);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
            }

            return ResponseEntity.ok(doctor);
        } catch (EntityNotFoundException e) {
            logger.error("Doctor not found for userId: {}", userId, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            logger.error("Error updating doctor for userId: {}", userId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    public Optional<Doctor> findByPseudo(String pseudo) {
        return doctorRepository.findByUserPseudo(pseudo);
    }

    public List<Doctor> findBySpecialization(String specialization) {
        return doctorRepository.findBySpecialization(specialization);
    }

    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    public Doctor getDoctorById(Long doctorTd) {
        return doctorRepository.findById(doctorTd).orElseThrow(() -> new IllegalArgumentException("Doctor not found"));
    }

    public Doctor saveDoctor(Doctor doctor) {
        return doctorRepository.save(doctor);
    }

    public void deleteDoctor(Long id) {
        doctorRepository.deleteById(id);
    }

    public Optional<Doctor> getDoctorByUserId(Long userId) {
        return doctorRepository.findByUserUserId(userId);
    }
}
