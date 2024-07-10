package com.sp.telemedecine.services.doctor;

import com.sp.telemedecine.models.Doctor;
import com.sp.telemedecine.models.DoctorAvailability;
import com.sp.telemedecine.repository.DoctorAvailabilityRepository;
import com.sp.telemedecine.repository.DoctorRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class DoctorAvailabilityService {

    @Autowired
    private DoctorAvailabilityRepository doctorAvailabilityRepository;
    @Autowired
    private DoctorRepo doctorRepo;
    @Autowired
    private DoctorService doctorService;

//    public ResponseEntity<?> saveDoctorAvailability(DoctorAvailability availability) {
//        LocalDate today = LocalDate.now();
//        LocalTime now = LocalTime.now();
//
//        if (availability.getDate().isBefore(today) ||
//                (availability.getDate().isEqual(today) && availability.getEndTime().isBefore(now))) {
//            throw new IllegalArgumentException("Cannot add availability for a past date or time.");
//        }
//
//        doctorAvailabilityRepository.save(availability);
//        return ResponseEntity.ok("Doctor availability saved successfully");
//    }
    public DoctorAvailability saveDoctorAvailability(DoctorAvailability availability) {
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();

        if (availability.getDate().isBefore(today) ||
                (availability.getDate().isEqual(today) && availability.getEndTime().isBefore(now))) {
            throw new IllegalArgumentException("Cannot add availability for a past date or time.");
        }
        List<DoctorAvailability> overlappingAvailabilities = doctorAvailabilityRepository.findOverlappingAvailabilities(
                availability.getDoctor().getId(),
                availability.getDate(),
                availability.getStartTime(),
                availability.getEndTime()
        );
//        if (!overlappingAvailabilities.isEmpty()) {
//            throw new IllegalArgumentException("Doctor already has availability during this time");
//        }

        return doctorAvailabilityRepository.save(availability);
    }

    public void updateAvailability(Long availabilityId, LocalDate date, LocalTime startTime, LocalTime endTime) {
        DoctorAvailability availability = doctorAvailabilityRepository.findById(availabilityId)
                .orElseThrow(() -> new IllegalArgumentException("No availability found for the given ID."));

        if (date.isBefore(LocalDate.now()) ||
                (date.isEqual(LocalDate.now()) && endTime.isBefore(LocalTime.now()))) {
            throw new IllegalArgumentException("Cannot update availability to a past date or time.");
        }

        List<DoctorAvailability> overlappingAvailabilities = doctorAvailabilityRepository.findOverlappingAvailabilities(
                availability.getDoctor().getId(), date, startTime, endTime);
        if (!overlappingAvailabilities.isEmpty() && !overlappingAvailabilities.get(0).getId().equals(availabilityId)) {
            throw new IllegalArgumentException("Doctor already has availability during this time.");
        }

        availability.setDate(date);
        availability.setStartTime(startTime);
        availability.setEndTime(endTime);

        doctorAvailabilityRepository.save(availability);
    }

    public List<DoctorAvailability> getDoctorAvailabilityByDoctorId(Long doctorId) {
        return doctorAvailabilityRepository.findByDoctorId(doctorId);
    }

    public List<DoctorAvailability> getDoctorAvailabilityByDoctorAndDate(Doctor doctor, LocalDate date) {
        return doctorAvailabilityRepository.findByDoctorAndDate(doctor, date);
    }

    public Optional<DoctorAvailability> getDoctorAvailabilityById(Long id) {
        return doctorAvailabilityRepository.findById(id);
    }

    public void deleteDoctorAvailability(Long id) {
        doctorAvailabilityRepository.deleteById(id);
    }


    public boolean updateAvailabilityStatus(Long doctorId, LocalDate date, LocalTime appointmentTime, boolean status) {
        Doctor doctor = doctorService.getDoctorById(doctorId);
        List<DoctorAvailability> availabilities = doctorAvailabilityRepository.findByDoctorAndDate(doctor, date);
        boolean updated = false;

        for (DoctorAvailability availability : availabilities) {
            if (!appointmentTime.isBefore(availability.getStartTime()) && !appointmentTime.isAfter(availability.getEndTime())) {
                availability.setTaken(status);
                saveDoctorAvailability(availability);
                updated = true;
            }
        }

        return updated;
    }

    public List<DoctorAvailability> getUpcomingAvailabilitiesByDoctor(Doctor doctor) {
        LocalDate today = LocalDate.now();
        return doctorAvailabilityRepository.findByDoctorAndDateAfterOrderByDateAscStartTimeAsc(doctor, today);
    }

    public Doctor getDoctorById(Long doctorId) {
        return doctorRepo.findById(doctorId).orElseThrow(() -> new RuntimeException("Doctor not found"));
    }

}
