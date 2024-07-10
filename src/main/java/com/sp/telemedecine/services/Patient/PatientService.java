package com.sp.telemedecine.services.Patient;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.sp.telemedecine.models.Patient;
import com.sp.telemedecine.repository.PatientRepo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class PatientService {

    @Autowired
    private PatientRepo patientRepository;
    @Autowired
    private Bucket bucket;

    @Autowired
    private EntityManager entityManager;

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
    public Patient updatePatient(Long userId, Patient patient, MultipartFile profileImage) throws IOException {
        Optional<Patient> existingPatient = getPatientByUserId(userId);
        if (existingPatient.isPresent()) {
            Patient existing = existingPatient.get();
            existing.setFirstName(patient.getFirstName());
            existing.setLastName(patient.getLastName());
            existing.setGender(patient.getGender());
            existing.setAddress(patient.getAddress());
            existing.setContactNumber(patient.getContactNumber());

            // Si une image de profil est fournie, téléchargez-la sur Firebase
            if (profileImage != null && !profileImage.isEmpty()) {
                String imageUrl = uploadImageToFirebase(profileImage, "Patient");
                existing.setProfilePictureUrl(imageUrl);
            }

            entityManager.merge(existing);
            return existing;
        }
        return null;
    }

    private String uploadImageToFirebase(MultipartFile profileImage, String entityPrefix) throws IOException {
        String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String randomNumbers = String.valueOf(new Random().nextInt(10000));
        String fileName = entityPrefix + "-" + timestamp + "-" + randomNumbers + "-" + profileImage.getOriginalFilename();
        Blob blob = bucket.create(fileName, profileImage.getInputStream(), profileImage.getContentType());
        return blob.getMediaLink();
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