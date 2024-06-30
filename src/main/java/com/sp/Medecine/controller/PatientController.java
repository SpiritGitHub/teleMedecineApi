package com.sp.Medecine.controller;

import com.sp.Medecine.models.Doctor;
import com.sp.Medecine.models.Patient;
import com.sp.Medecine.services.Patient.PatientService;
import com.sp.Medecine.services.doctor.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/patient")
public class PatientController {

    @Autowired
    private PatientService patientService;

    @Autowired
    private DoctorService doctorService;

    @GetMapping("/hipatient")
    public ResponseEntity<String> sayHello(){
        return ResponseEntity.ok("Hello patient");
    }

    @DeleteMapping("/{id}")
    public void deletePatient(@PathVariable Long id) {
        patientService.deletePatient(id);
    }

    @GetMapping("/find-doctor-username/{username}")
    public Optional<Doctor> getDoctorByUsername(@PathVariable String username) {
        return doctorService.findByPseudo(username);
    }

    @GetMapping("/find-doctor-specialization/{specialization}")
    public List<Doctor> getDoctorsBySpecialization(@PathVariable String specialization) {
        return doctorService.findBySpecialization(specialization);
    }

    @GetMapping("/user/{userId}")
    public Optional<Patient> getPatientByUserId(@PathVariable Long userId) {
        return patientService.getPatientByUserId(userId);
    }

    @PutMapping("/update-profil-patient/{userId}")
    public Patient updatePatient(@PathVariable Long userId, @RequestBody Patient patient) {
        Optional<Patient> existingPatient = patientService.getPatientByUserId(userId);
        if (existingPatient.isPresent()) {
            patient.setId(existingPatient.get().getId());
            patient.setUser(existingPatient.get().getUser());
            patient.setAddress(patient.getAddress());
            patient.setBirthday(patient.getBirthday());
            patient.setGender(patient.getGender());
            patient.setContactNumber(patient.getContactNumber());
            patient.setFirstName(patient.getFirstName());
            patient.setLastName(patient.getLastName());
            return patientService.savePatient(patient);
        }
        return null;
    }
}