package com.sp.telemedecine.services.Autre;

import com.sp.telemedecine.dto.BookAppointmentRequest;
import com.sp.telemedecine.dto.CancelAppointmentRequest;
import com.sp.telemedecine.dto.CompleteAppointmentRequest;
import com.sp.telemedecine.models.*;
import com.sp.telemedecine.repository.AppointmentRepository;
import com.sp.telemedecine.repository.DoctorAvailabilityRepository;
import com.sp.telemedecine.repository.DoctorNoteRepository;
import com.sp.telemedecine.repository.PrescriptionRepository;
import com.sp.telemedecine.services.Patient.PatientService;
import com.sp.telemedecine.services.doctor.DoctorAvailabilityService;
import com.sp.telemedecine.services.doctor.DoctorService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private DoctorNoteRepository doctorNoteRepository;

    @Autowired
    private PrescriptionRepository prescriptionRepository;

    @Autowired
    private DoctorAvailabilityRepository doctorAvailabilityRepository;

    @Autowired
    private DoctorAvailabilityService doctorAvailabilityService;

    @Autowired
    private PatientService patientService;

    @Autowired
    private DoctorService doctorService;

    @Transactional
    public ResponseEntity<String> bookAppointment(BookAppointmentRequest request) {
        if (request == null || request.getPatientId() == null || request.getDoctorId() == null
                || request.getAppointmentDate() == null || request.getAppointmentTime() == null) {
            throw new IllegalArgumentException("Invalid appointment request: Missing required fields.");
        }

        Patient patient = patientService.getPatientById(request.getPatientId());
        Doctor doctor = doctorService.getDoctorById(request.getDoctorId());

        if (patient == null || doctor == null) {
            throw new IllegalArgumentException("Invalid patient or doctor ID.");
        }

        LocalDate appointmentDate = request.getAppointmentDate();
        LocalTime appointmentTime = request.getAppointmentTime();

        // Check if the appointment date is in the past
        if (appointmentDate.isBefore(LocalDate.now()) ||
                (appointmentDate.equals(LocalDate.now()) && appointmentTime.isBefore(LocalTime.now()))) {
            throw new IllegalArgumentException("Cannot book an appointment for a past date or time.");
        }

        // Check if doctor is available
        List<DoctorAvailability> availabilities = doctorAvailabilityService.getDoctorAvailabilityByDoctorAndDate(doctor, appointmentDate);
        boolean isAvailable = availabilities.stream()
                .anyMatch(a -> !a.isTaken() &&
                        !appointmentTime.isBefore(a.getStartTime()) &&
                        !appointmentTime.isAfter(a.getEndTime()));

        if (!isAvailable) {
            throw new IllegalArgumentException("The doctor is not available at the selected time.");
        }

        // Mark slot as taken
        boolean updated = doctorAvailabilityService.updateAvailabilityStatus(doctor.getId(), appointmentDate, appointmentTime, true);
        if (!updated) {
            throw new IllegalStateException("Failed to mark the doctor as unavailable at the selected time.");
        }

        // Create and save appointment
        Appointment appointment = new Appointment();
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setAppointmentDate(appointmentDate);
        appointment.setAppointmentTime(appointmentTime);
        appointment.setReason(request.getReason());
        appointment.setStatus(AppointmentStatus.SCHEDULED);

        appointmentRepository.save(appointment);

        return ResponseEntity.ok("Appointment booked successfully with ID: " + appointment.getId());
    }


    @Transactional
    public Appointment cancelAppointment(CancelAppointmentRequest request) {
        Optional<Appointment> optionalAppointment = appointmentRepository.findById(request.getAppointmentId());
        if (optionalAppointment.isPresent()) {
            Appointment appointment = optionalAppointment.get();
            appointment.setStatus(AppointmentStatus.CANCELLED);
            appointmentRepository.save(appointment);
            doctorAvailabilityService.updateAvailabilityStatus(appointment.getDoctor().getId(), appointment.getAppointmentDate(), appointment.getAppointmentTime(), false);
            return appointment;
        } else {
            throw new IllegalArgumentException("Appointment not found");
        }
    }

    @Transactional
    public Appointment completeAppointment(CompleteAppointmentRequest request) {
        Optional<Appointment> optionalAppointment = appointmentRepository.findById(request.getAppointmentId());
        if (optionalAppointment.isPresent()) {
            Appointment appointment = optionalAppointment.get();

            DoctorNote doctorNote = request.getDoctorNote();
            if (doctorNote != null) {
                doctorNote.setAppointment(appointment);
                doctorNoteRepository.save(doctorNote);
            }

            List<Prescription> prescriptions = request.getPrescriptions().stream().map(dto -> {
                Prescription prescription = new Prescription();
                prescription.setMedication(dto.getMedication());
                prescription.setDosage(dto.getDosage());
                prescription.setFrequency(dto.getFrequency());
                prescription.setInstructions(dto.getInstructions());
                prescription.setAppointment(appointment);
                return prescription;
            }).collect(Collectors.toList());

            if (!prescriptions.isEmpty()) {
                for (Prescription prescription : prescriptions) {
                    Optional<Prescription> existingPrescription = prescriptionRepository.findByAppointmentAndMedication(appointment, prescription.getMedication());
                    if (existingPrescription.isPresent()) {
                        Prescription existing = existingPrescription.get();
                        existing.setDosage(prescription.getDosage());
                        existing.setFrequency(prescription.getFrequency());
                        existing.setInstructions(prescription.getInstructions());
                        prescriptionRepository.save(existing);
                    } else {
                        Optional<Prescription> duplicatePrescription = prescriptionRepository.findByAppointmentId(appointment.getId());
                        if (duplicatePrescription.isPresent()) {
                            throw new DuplicatePrescriptionException("Une prescription avec cet appointment_id existe déjà.");
                        } else {
                            prescriptionRepository.save(prescription);
                        }
                    }
                }
            }

            appointment.setStatus(AppointmentStatus.COMPLETED);
            return appointmentRepository.save(appointment);
        } else {
            throw new EntityNotFoundException("Appointment not found");
        }
    }





//    public Appointment saveAppointment(Appointment appointment) {
//        if (appointment.getStatus() == null) {
//            appointment.setStatus(AppointmentStatus.SCHEDULED);
//        }
//        return appointmentRepository.save(appointment);
//    }

    public void deleteAppointment(Long id) {
        appointmentRepository.deleteById(id);
    }

    public Appointment updateAppointmentStatus(Long id, AppointmentStatus status) {
        Optional<Appointment> appointment = appointmentRepository.findById(id);
        if (appointment.isPresent()) {
            Appointment updatedAppointment = appointment.get();
            updatedAppointment.setStatus(status);
            return appointmentRepository.save(updatedAppointment);
        }
        return null;
    }

//    public Appointment completeAppointment(Long id, DoctorNote doctorNote, List<Prescription> prescriptions) {
//        Optional<Appointment> optionalAppointment = appointmentRepository.findById(id);
//        if (optionalAppointment.isPresent()) {
//            Appointment appointment = optionalAppointment.get();
//
//            // Enregistrer la note du médecin si elle existe
//            if (doctorNote != null) {
//                doctorNote.setAppointment(appointment);
//                doctorNoteRepository.save(doctorNote);
//            }
//
//            // Enregistrer les prescriptions si elles existent
//            if (prescriptions != null && !prescriptions.isEmpty()) {
//                for (Prescription prescription : prescriptions) {
//                    prescription.setAppointment(appointment);
//                    prescriptionRepository.save(prescription);
//                }
//            }
//
//            appointment.setStatus(AppointmentStatus.COMPLETED);
//            return appointmentRepository.save(appointment);
//        }
//        return null;
//    }

    public List<Appointment> getAppointmentsByPatient(Patient patient) {
        return appointmentRepository.findByPatient(patient);
    }

    public List<Appointment> getAppointmentsByDoctor(Doctor doctor) {
        return appointmentRepository.findByDoctor(doctor);
    }

    public Optional<Appointment> getAppointmentById(Long id) {
        return appointmentRepository.findById(id);
    }


    public List<Appointment> getPastAppointmentsByPatient(Patient patient) {
        LocalDateTime now = LocalDateTime.now();
        LocalDate date = now.toLocalDate();
        LocalTime time = now.toLocalTime();
        return appointmentRepository.findByPatientAndAppointmentDateTimeBefore(patient, date, time);
    }

    public List<Appointment> getUpcomingAppointmentsByPatient(Patient patient) {
        LocalDateTime now = LocalDateTime.now();
        LocalDate date = now.toLocalDate();
        LocalTime time = now.toLocalTime();
        return appointmentRepository.findByPatientAndAppointmentDateTimeAfter(patient, date, time);
    }

    public List<Appointment> getCancelledOrCompletedAppointmentsByPatient(Patient patient) {
        return appointmentRepository.findByPatientAndStatusIn(patient, Arrays.asList(AppointmentStatus.CANCELLED, AppointmentStatus.COMPLETED));
    }

    public List<Appointment> getPastAppointmentsByDoctor(Doctor doctor) {
        LocalDateTime now = LocalDateTime.now();
        LocalDate date = now.toLocalDate();
        LocalTime time = now.toLocalTime();
        return appointmentRepository.findByDoctorAndAppointmentDateTimeBefore(doctor, date, time);
    }

    public List<Appointment> getUpcomingAppointmentsByDoctor(Doctor doctor) {
        LocalDateTime now = LocalDateTime.now();
        LocalDate date = now.toLocalDate();
        LocalTime time = now.toLocalTime();
        return appointmentRepository.findByDoctorAndAppointmentDateTimeAfter(doctor, date, time);
    }

    public List<Appointment> getCancelledOrCompletedAppointmentsByDoctor(Doctor doctor) {
        return appointmentRepository.findByDoctorAndStatusIn(doctor, Arrays.asList(AppointmentStatus.CANCELLED, AppointmentStatus.COMPLETED));
    }


}
