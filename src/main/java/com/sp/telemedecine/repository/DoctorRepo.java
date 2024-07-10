package com.sp.telemedecine.repository;

import com.sp.telemedecine.models.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorRepo extends JpaRepository<Doctor, Long> {
    Optional<Doctor> findByUserPseudo(String pseudo);
    Optional<Doctor> findByUserUserId(Long userId);
    List<Doctor> findBySpecialization(String specialization);
}
