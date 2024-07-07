package com.sp.telemedecine.controller;

import com.sp.telemedecine.dto.BookAppointmentRequest;
import com.sp.telemedecine.dto.CancelAppointmentRequest;
import com.sp.telemedecine.services.Autre.AppointmentService;
//import com.sp.Medecine.services.Autre.MedicalHistoryService;
import com.sp.telemedecine.services.Autre.PrescriptionService;
import com.sp.telemedecine.services.Patient.PatientService;
import com.sp.telemedecine.services.doctor.DoctorNoteService;

import com.sp.telemedecine.models.*;
import com.sp.telemedecine.services.doctor.DoctorService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    @Autowired
    private AppointmentService appointmentService;
//
//    @Autowired
//    private MedicalHistoryService medicalHistoryService;

    @Autowired
    private PrescriptionService prescriptionService;

    @Autowired
    private DoctorNoteService doctorNoteService;



    @PostMapping("/book-appointment")
    public ResponseEntity<String> bookAppointment(@RequestBody BookAppointmentRequest request) {
        try {
            return appointmentService.bookAppointment(request);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            // Catch any other unexpected exceptions
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred: " + e.getMessage());
        }
    }

    @GetMapping("/appointment-patient/{patientId}")
    public List<Appointment> getAppointmentsByPatientId(@PathVariable Long patientId) {
        Patient patient = patientService.getPatientById(patientId);
        return appointmentService.getAppointmentsByPatient(patient);
    }

    @GetMapping("/appointment-patient/{patientId}/past")
    public ResponseEntity<List<Appointment>> getPastAppointmentsByPatient(@PathVariable Long patientId) {
        Patient patient = patientService.getPatientById(patientId);
        List<Appointment> appointments = appointmentService.getPastAppointmentsByPatient(patient);
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/appointment-patient/{patientId}/upcoming")
    public ResponseEntity<List<Appointment>> getUpcomingAppointmentsByPatient(@PathVariable Long patientId) {
        Patient patient = patientService.getPatientById(patientId);
        List<Appointment> appointments = appointmentService.getUpcomingAppointmentsByPatient(patient);
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/appointment-patient/{patientId}/cancelled-completed")
    public ResponseEntity<List<Appointment>> getCancelledOrCompletedAppointmentsByPatient(@PathVariable Long patientId) {
        Patient patient = patientService.getPatientById(patientId);
        List<Appointment> appointments = appointmentService.getCancelledOrCompletedAppointmentsByPatient(patient);
        return ResponseEntity.ok(appointments);
    }

    @PutMapping("/cancel-appointment/{id}")
    public ResponseEntity<Void> cancelAppointment(@RequestBody CancelAppointmentRequest request) {
        appointmentService.cancelAppointment(request);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("appointment/{id}")
    public ResponseEntity<Optional<Appointment>> getAppointmentById(@PathVariable Long id) {
        return ResponseEntity.ok(appointmentService.getAppointmentById(id));
    }
///////////////////////////////////////////////////////////////////////////////////////////
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

    @PutMapping("/update-profil-patient/{id}")
    public ResponseEntity<Patient> updatePatientDetails(@PathVariable Long id, @RequestBody Patient patientDetails) {
        try {
            Patient updatedPatient = patientService.updatePatient(id, patientDetails);
            return new ResponseEntity<>(updatedPatient, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
////////////////////////////////////////////////MedicalHistory//////////////////////////////////////
//    @GetMapping("/medical-hitory-patient/{patientId}")
//    public List<MedicalHistory> getMedicalHistoriesByPatientId(@PathVariable Long patientId) {
//        return medicalHistoryService.getMedicalHistoriesByPatientId(patientId);
//    }
///////////////////////////////////////////////Prescription//////////////////////////////////////////
//
//    @GetMapping("/prescripion-by-appointment/{appointmentId}")
//    public ResponseEntity<List<Prescription>> getPrescriptionsByAppointmentId(@PathVariable Long appointmentId) {
//        return ResponseEntity.ok(prescriptionService.getPrescriptionsByAppointmentId(appointmentId));
//    }
//    @GetMapping("prescription-by-patient/{patientId}")
//    public ResponseEntity<Prescription> getPrescriptionByPatientId(@PathVariable Long patientId) {
//        return ResponseEntity.ok((Prescription) prescriptionService.getPrescriptionsByPatientId(patientId));
//    }
/////////////////////////////////////////Docteur Note ////////////////////////////////////////////////
//    @GetMapping("/by-appointment/{appointmentId}")
//    public ResponseEntity<List<DoctorNote>> getDoctorNotesByAppointmentId(@PathVariable Long appointmentId) {
//        List<DoctorNote> doctorNotes = doctorNoteService.getDoctorNotesByAppointmentId(appointmentId);
//        return ResponseEntity.ok(doctorNotes);
//    }
}