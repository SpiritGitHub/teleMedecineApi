package com.sp.telemedecine.services.doctor;

import com.sp.telemedecine.models.Doctor;
import com.sp.telemedecine.repository.DoctorRepo;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class DoctorService {

    @Autowired
    private DoctorRepo doctorRepository;


    @Autowired
    private EntityManager entityManager;

    @Transactional
    public Doctor updateDoctor(Long userId, Doctor doctor) {
        Optional<Doctor> existingDoctor = getDoctorByUserId(userId);
        if (existingDoctor.isPresent()) {
            Doctor existing = existingDoctor.get();
            existing.setSpecialization(doctor.getSpecialization());
            existing.setGender(doctor.getGender());
            existing.setAddress(doctor.getAddress());
            existing.setContactNumber(doctor.getContactNumber());
            entityManager.merge(existing);
            return existing;
        }
        return null;
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
