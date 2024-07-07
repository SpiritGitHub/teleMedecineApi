package com.sp.telemedecine.repository;

import com.sp.telemedecine.models.AppointmentStatus;
import com.sp.telemedecine.models.Appointment;
import com.sp.telemedecine.models.Doctor;
import com.sp.telemedecine.models.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByDoctorAndAppointmentDate(Doctor doctor, LocalDate appointmentDate);
    List<Appointment> findByPatient(Patient patient);
    List<Appointment> findByDoctor(Doctor doctor);

    List<Appointment> findByStatus(AppointmentStatus status);
    List<Appointment> findByPatientAndAppointmentDateBeforeAndAppointmentTimeBefore(Patient patient, LocalDate appointmentDate, LocalTime appointmentTime);
    @Query("SELECT a FROM Appointment a WHERE a.patient = :patient AND (a.appointmentDate < :date OR (a.appointmentDate = :date AND a.appointmentTime < :time))")
    List<Appointment> findByPatientAndAppointmentDateTimeBefore(@Param("patient") Patient patient, @Param("date") LocalDate date, @Param("time") LocalTime time);

    @Query("SELECT a FROM Appointment a WHERE a.patient = :patient AND (a.appointmentDate > :date OR (a.appointmentDate = :date AND a.appointmentTime > :time))")
    List<Appointment> findByPatientAndAppointmentDateTimeAfter(@Param("patient") Patient patient, @Param("date") LocalDate date, @Param("time") LocalTime time);

    @Query("SELECT a FROM Appointment a WHERE a.doctor = :doctor AND (a.appointmentDate < :date OR (a.appointmentDate = :date AND a.appointmentTime < :time))")
    List<Appointment> findByDoctorAndAppointmentDateTimeBefore(@Param("doctor") Doctor doctor, @Param("date") LocalDate date, @Param("time") LocalTime time);

    @Query("SELECT a FROM Appointment a WHERE a.doctor = :doctor AND (a.appointmentDate > :date OR (a.appointmentDate = :date AND a.appointmentTime > :time))")
    List<Appointment> findByDoctorAndAppointmentDateTimeAfter(@Param("doctor") Doctor doctor, @Param("date") LocalDate date, @Param("time") LocalTime time);

    List<Appointment> findByPatientAndStatusIn(Patient patient, List<AppointmentStatus> statuses);

    List<Appointment> findByDoctorAndStatusIn(Doctor doctor, List<AppointmentStatus> statuses);
}
