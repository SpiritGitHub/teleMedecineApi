package com.sp.Medecine.services.doctor;

import com.sp.Medecine.models.Doctor;
import com.sp.Medecine.models.Patient;
import com.sp.Medecine.repository.DoctorRepo;
import com.sp.Medecine.repository.PatientRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DoctorService {

    @Autowired
    private DoctorRepo doctorRepository;


    public Optional<Doctor> findByPseudo(String pseudo) {
        return doctorRepository.findByUserPseudo(pseudo);
    }

    public List<Doctor> findBySpecialization(String specialization) {
        return doctorRepository.findBySpecialization(specialization);
    }

    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    public Doctor getDoctorById(Long id) {
        return doctorRepository.findById(id).orElse(null);
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
