package com.sp.telemedecine.repository;

import com.sp.telemedecine.models.DoctorNote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoctorNoteRepository extends JpaRepository<DoctorNote, Long> {
    List<DoctorNote> findByAppointment_Doctor_Id(Long doctorId);

    List<DoctorNote> findByAppointmentId(Long appointmentId);
}
