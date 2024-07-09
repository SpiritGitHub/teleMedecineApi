package com.sp.telemedecine.repository;

import com.sp.telemedecine.models.Appointment;
import com.sp.telemedecine.models.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {
//    List<Prescription> findByAppointmentsId(Long appointmentId);
    List<Prescription> findByAppointment_Doctor_Id(Long doctorId);
    List<Prescription> findByAppointment_Patient_Id(Long patientId);
    Optional<Prescription> findByAppointmentAndMedication(Appointment appointment, String medication);
    Optional<Prescription> findByAppointmentId(Long appointmentId);
}
