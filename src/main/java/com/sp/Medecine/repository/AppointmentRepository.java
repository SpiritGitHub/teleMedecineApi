package com.sp.Medecine.repository;

import com.sp.Medecine.models.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    @Query("SELECT a FROM Appointment a WHERE a.patient.id = :patientId")
    List<Appointment> getAppointmentsOfPatient(@Param("patientId") Long patientId);
}
