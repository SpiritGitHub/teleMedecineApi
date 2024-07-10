package com.sp.telemedecine.controller;

import com.sp.telemedecine.dto.CancelAppointmentRequest;
import com.sp.telemedecine.dto.CompleteAppointmentRequest;
import com.sp.telemedecine.models.*;
import com.sp.telemedecine.services.Autre.AppointmentService;
import com.sp.telemedecine.services.Autre.MedicalHistoryService;
import com.sp.telemedecine.services.Autre.PrescriptionService;
import com.sp.telemedecine.services.Patient.PatientService;
import com.sp.telemedecine.services.doctor.DoctorAvailabilityService;
import com.sp.telemedecine.services.doctor.DoctorNoteService;
import com.sp.telemedecine.services.doctor.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/doctor")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private DoctorNoteService doctorNoteService;
    @Autowired
    private DoctorAvailabilityService doctorAvailabilityService;

    @Autowired
    private PatientService patientService;

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private MedicalHistoryService medicalHistoryService;

    @Autowired
    private PrescriptionService prescriptionService;

/////////////////////////////////////////////MedicalHistory/////////////////////////////////////////
//    @PostMapping
//    public MedicalHistory createMedicalHistory(@RequestBody MedicalHistory medicalHistory) {
//        return medicalHistoryService.createMedicalHistory(medicalHistory);
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<MedicalHistory> updateMedicalHistory(@PathVariable Long id, @RequestBody MedicalHistory medicalHistoryDetails) {
//        MedicalHistory updatedMedicalHistory = medicalHistoryService.updateMedicalHistory(id, medicalHistoryDetails);
//        return updatedMedicalHistory != null ? ResponseEntity.ok(updatedMedicalHistory) : ResponseEntity.notFound().build();
//    }
////////////////////////////////////////////////Appointment////////////////////////////////////////////
    @PutMapping("/cancel-appointment/{id}")
    public ResponseEntity<Void> cancelAppointment(@RequestBody CancelAppointmentRequest request) {
        appointmentService.cancelAppointment(request);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("all-doctor-appointment/{doctorId}")
    public ResponseEntity<List<Appointment>> getAppointmentByDoctor(@PathVariable Long doctorId){
        Doctor doctor = new Doctor();
        doctor.setId(doctorId);
        return ResponseEntity.ok(appointmentService.getAppointmentsByDoctor(doctor));
    }

    @PostMapping("complete-appintment/{id}/complete")
    public ResponseEntity<Void> completeAppointment(@RequestBody CompleteAppointmentRequest request) {
        appointmentService.completeAppointment(request);
        return ResponseEntity.noContent().build();
    }

//    @GetMapping("/appointment-patient/{patientId}")
//    public ResponseEntity<List<Appointment>> getAppointmentsByPatient(@PathVariable Long patientId) {
//        Patient patient = new Patient();
//        patient.setId(patientId);
//        return ResponseEntity.ok(appointmentService.getAppointmentsByPatient(patient));
//    }

    @GetMapping("appointment/{id}")
    public ResponseEntity<Optional<Appointment>> getAppointmentById(@PathVariable Long id) {
        return ResponseEntity.ok(appointmentService.getAppointmentById(id));
    }

    @GetMapping("/doctor-appointment/{doctorId}/past")
    public ResponseEntity<List<Appointment>> getPastAppointmentsByDoctor(@PathVariable Long doctorId) {
        Doctor doctor = doctorService.getDoctorById(doctorId);
        List<Appointment> appointments = appointmentService.getPastAppointmentsByDoctor(doctor);
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/doctor-appointment/{doctorId}/upcoming")
    public ResponseEntity<List<Appointment>> getUpcomingAppointmentsByDoctor(@PathVariable Long doctorId) {
        Doctor doctor = doctorService.getDoctorById(doctorId);
        List<Appointment> appointments = appointmentService.getUpcomingAppointmentsByDoctor(doctor);
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/doctor-appointment/{doctorId}/cancelled-completed")
    public ResponseEntity<List<Appointment>> getCancelledOrCompletedAppointmentsByDoctor(@PathVariable Long doctorId) {
        Doctor doctor = doctorService.getDoctorById(doctorId);
        List<Appointment> appointments = appointmentService.getCancelledOrCompletedAppointmentsByDoctor(doctor);
        return ResponseEntity.ok(appointments);
    }
/////////////////////////////////////////////Availability/////////////////////////////////////////////

    @PostMapping(value = "/add-availability", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createDoctorAvailability(@RequestBody DoctorAvailability availability) {
        try {
            DoctorAvailability savedAvailability = doctorAvailabilityService.saveDoctorAvailability(availability);
            return new ResponseEntity<>(savedAvailability, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/update-availability/{availabilityId}")
    public ResponseEntity<String> updateDoctorAvailability(
            @PathVariable Long availabilityId,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam("startTime") @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime startTime,
            @RequestParam("endTime") @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime endTime) {

        try {
            doctorAvailabilityService.updateAvailability(availabilityId, date, startTime, endTime);
            return ResponseEntity.ok("Availability updated successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/upcoming")
    public ResponseEntity<List<DoctorAvailability>> getUpcomingAvailabilitiesByDoctor(@RequestParam Long doctorId) {
        Doctor doctor = doctorAvailabilityService.getDoctorById(doctorId);
        List<DoctorAvailability> availabilities = doctorAvailabilityService.getUpcomingAvailabilitiesByDoctor(doctor);
        return ResponseEntity.ok(availabilities);
    }

    @GetMapping("/all-availability-by-doctor/{doctorId}")
    public List<DoctorAvailability> getDoctorAvailabilityByDoctorId(@PathVariable Long doctorId) {
        return doctorAvailabilityService.getDoctorAvailabilityByDoctorId(doctorId);
    }

    @GetMapping("/availability-by-id/{id}")
    public Optional<DoctorAvailability> getDoctorAvailabilityById(@PathVariable Long id) {
        return doctorAvailabilityService.getDoctorAvailabilityById(id);
    }



    @DeleteMapping("/delete-availability/{id}")
    public void deleteDoctorAvailability(@PathVariable Long id) {
        doctorAvailabilityService.deleteDoctorAvailability(id);
    }

//////////////////////////////////////////Profile//////////////////////////////////////////////////////
    @GetMapping("/hidoctor")
    public ResponseEntity<String> sayHello(){
        return ResponseEntity.ok("Hello doctor");
    }

    @PutMapping("/update-profil-doctor/{userId}")
    public ResponseEntity<Doctor> updateDoctor(@PathVariable Long userId, @RequestBody Doctor doctor, @RequestParam("profileImage") MultipartFile profileImage) {
        try {
            Doctor updatedDoctor = doctorService.updateDoctor(userId, doctor, profileImage);
            return ResponseEntity.ok(updatedDoctor);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }
    @DeleteMapping("delete-doctor/{id}")
    public void deleteDoctor(@PathVariable Long id) {
        doctorService.deleteDoctor(id);
    }


//    @GetMapping("/find-patient-username/{username}")
//    public Optional<Patient> getPatientByUsername(@PathVariable String username) {
//        return patientService.findByUsername(username);
//    }

////////////////////////////////////////////////////////////////////////////////////////////////
//    @GetMapping("/doctor-note/{doctorId}")
//    public List<DoctorNote> getDoctorNotesByDoctorId(@PathVariable Long doctorId) {
//        return doctorNoteService.getDoctorNotesByDoctorId(doctorId);
//    }
//    @GetMapping("/by-appointment/{appointmentId}")
//    public ResponseEntity<List<DoctorNote>> getDoctorNotesByAppointmentId(@PathVariable Long appointmentId) {
//        List<DoctorNote> doctorNotes = doctorNoteService.getDoctorNotesByAppointmentId(appointmentId);
//        return ResponseEntity.ok(doctorNotes);
//    }

///////////////////////////////////////////Prescription////////////////////////////////////////

//    @PostMapping("creeate-prescription")
//    public ResponseEntity<Prescription> createPrescription(@RequestBody Prescription prescription) {
//        return ResponseEntity.ok(prescriptionService.createPrescription(prescription));
//    }
//
//    @PutMapping("update-prescription/{id}")
//    public ResponseEntity<Prescription> updatePrescription(@PathVariable Long id, @RequestBody Prescription prescriptionDetails) {
//        Prescription updatedPrescription = prescriptionService.updatePrescription(id, prescriptionDetails);
//        return updatedPrescription != null ? ResponseEntity.ok(updatedPrescription) : ResponseEntity.notFound().build();
//    }
//
//    @GetMapping("prescription-by-patient/{patientId}")
//    public ResponseEntity<Prescription> getPrescriptionByPatientId(@PathVariable Long doctorId) {
//        return ResponseEntity.ok((Prescription) prescriptionService.getPrescriptionsByDoctorId(doctorId));
//    }
//
//    @GetMapping("/prescripion-by-appointment/{appointmentId}")
//    public ResponseEntity<List<Prescription>> getPrescriptionsByAppointmentId(@PathVariable Long appointmentId) {
//        return ResponseEntity.ok(prescriptionService.getPrescriptionsByAppointmentId(appointmentId));
//    }

}