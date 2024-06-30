package com.sp.Medecine.controller;

import com.sp.Medecine.models.DayOfWeek;
import com.sp.Medecine.models.Doctor;
import com.sp.Medecine.models.DoctorAvailability;
import com.sp.Medecine.models.Patient;
import com.sp.Medecine.services.Patient.PatientService;
import com.sp.Medecine.services.doctor.DoctorAvailabilityService;
import com.sp.Medecine.services.doctor.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/doctor")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private PatientService patientService;

    @Autowired
    private DoctorAvailabilityService doctorAvailabilityService;

    @PostMapping("/add-availability")
    public DoctorAvailability createDoctorAvailability(@RequestBody DoctorAvailability availability) {
        return doctorAvailabilityService.saveDoctorAvailability(availability);
    }

    @GetMapping("/all-availability-by-doctor/{doctorId}")
    public List<DoctorAvailability> getDoctorAvailabilityByDoctorId(@PathVariable Long doctorId) {
        return doctorAvailabilityService.getDoctorAvailabilityByDoctorId(doctorId);
    }

    @GetMapping("/availability-doctor-by-day/{doctorId}/day/{dayOfWeek}")
    public List<DoctorAvailability> getDoctorAvailabilityByDoctorIdAndDayOfWeek(@PathVariable Long doctorId, @PathVariable DayOfWeek dayOfWeek) {
        return doctorAvailabilityService.getDoctorAvailabilityByDoctorIdAndDayOfWeek(doctorId, dayOfWeek);
    }

    @GetMapping("/availability-by-id/{id}")
    public Optional<DoctorAvailability> getDoctorAvailabilityById(@PathVariable Long id) {
        return doctorAvailabilityService.getDoctorAvailabilityById(id);
    }

    @PutMapping("/update-availability/{id}")
    public DoctorAvailability updateDoctorAvailability(@PathVariable Long id, @RequestBody DoctorAvailability availability) {
        Optional<DoctorAvailability> existingAvailability = doctorAvailabilityService.getDoctorAvailabilityById(id);
        if (existingAvailability.isPresent()) {
            availability.setId(id);
            return doctorAvailabilityService.saveDoctorAvailability(availability);
        }
        return null;
    }

    @DeleteMapping("/delete-availability/{id}")
    public void deleteDoctorAvailability(@PathVariable Long id) {
        doctorAvailabilityService.deleteDoctorAvailability(id);
    }


    @GetMapping("/hidoctor")
    public ResponseEntity<String> sayHello(){
        return ResponseEntity.ok("Hello doctor");
    }


    @PutMapping("/update-profil-doctor/{userId}")
    public Doctor updateDoctor(@PathVariable Long userId, @RequestBody Doctor doctor) {
        Optional<Doctor> existingDoctor = doctorService.getDoctorByUserId(userId);
        if (existingDoctor.isPresent()) {
            doctor.setId(existingDoctor.get().getId());
            doctor.setUser(existingDoctor.get().getUser());
            doctor.setGender(doctor.getGender());
            doctor.setAddress(doctor.getAddress());
            doctor.setContactNumber(doctor.getContactNumber());
            doctor.setSpecialization(doctor.getSpecialization());
            return doctorService.saveDoctor(doctor);
        }
        return null;
    }

    @DeleteMapping("delete-doctor/{id}")
    public void deleteDoctor(@PathVariable Long id) {
        doctorService.deleteDoctor(id);
    }

    @GetMapping("/find-doctor-username/{username}")
    public Optional<Doctor> getDoctorByUsername(@PathVariable String username) {
        return doctorService.findByPseudo(username);
    }

    @GetMapping("/find-doctor-specialization/{specialization}")
    public List<Doctor> getDoctorsBySpecialization(@PathVariable String specialization) {
        return doctorService.findBySpecialization(specialization);
    }

    @GetMapping("/find-patient-username/{username}")
    public Optional<Patient> getPatientByUsername(@PathVariable String username) {
        return patientService.findByUsername(username);
    }
}