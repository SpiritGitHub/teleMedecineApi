package com.sp.telemedecine.services.doctor;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.sp.telemedecine.models.Doctor;
import com.sp.telemedecine.repository.DoctorRepo;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class DoctorService {

    @Autowired
    private DoctorRepo doctorRepository;


    @Autowired
    private EntityManager entityManager;

    @Autowired
    private Bucket bucket;

    @Transactional
    public Doctor updateDoctor(Long userId, Doctor doctor, MultipartFile profileImage) throws IOException {
        Optional<Doctor> existingDoctor = getDoctorByUserId(userId);
        if (existingDoctor.isPresent()) {
            Doctor existing = existingDoctor.get();
            existing.setSpecialization(doctor.getSpecialization());
            existing.setGender(doctor.getGender());
            existing.setAddress(doctor.getAddress());
            existing.setContactNumber(doctor.getContactNumber());

            // Si une image de profil est fournie, téléchargez-la sur Firebase
            if (profileImage != null && !profileImage.isEmpty()) {
                String imageUrl = uploadImageToFirebase(profileImage, "Doctor");
                existing.setProfileImageUrl(imageUrl);
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
