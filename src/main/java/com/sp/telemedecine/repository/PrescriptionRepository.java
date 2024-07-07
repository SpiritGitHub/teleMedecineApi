package com.sp.telemedecine.repository;

import com.sp.telemedecine.models.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {
    List<Prescription> findByAppointmentId(Long appointmentId);
    List<Prescription> findByAppointment_Doctor_Id(Long doctorId);
    List<Prescription> findByAppointment_Patient_Id(Long patientId);

}
