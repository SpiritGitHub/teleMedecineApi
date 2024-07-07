package com.sp.telemedecine.repository;

import com.sp.telemedecine.models.Doctor;
import com.sp.telemedecine.models.DoctorAvailability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorAvailabilityRepository extends JpaRepository<DoctorAvailability, Long> {
    List<DoctorAvailability> findByDoctorId(Long doctorId);

    List<DoctorAvailability> findByDoctorAndDate(Doctor doctor, LocalDate date);

    List<DoctorAvailability> findByDoctorAndDateAfterOrderByDateAscStartTimeAsc(Doctor doctor, LocalDate date);
    Optional<DoctorAvailability> findFirstByDoctorAndDateOrderByStartTimeAsc(Doctor doctor, LocalDate date);
    @Query("SELECT da FROM DoctorAvailability da WHERE da.doctor.id = :doctorId AND da.date = :date AND ((:startTime BETWEEN da.startTime AND da.endTime) OR (:endTime BETWEEN da.startTime AND da.endTime) OR (da.startTime BETWEEN :startTime AND :endTime) OR (da.endTime BETWEEN :startTime AND :endTime))")
    List<DoctorAvailability> findOverlappingAvailabilities(@Param("doctorId") Long doctorId, @Param("date") LocalDate date, @Param("startTime") LocalTime startTime, @Param("endTime") LocalTime endTime);

}

