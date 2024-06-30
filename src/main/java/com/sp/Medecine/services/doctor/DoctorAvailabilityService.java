package com.sp.Medecine.services.doctor;

import com.sp.Medecine.models.DayOfWeek;
import com.sp.Medecine.models.DoctorAvailability;
import com.sp.Medecine.repository.DoctorAvailabilityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DoctorAvailabilityService {

    @Autowired
    private DoctorAvailabilityRepository doctorAvailabilityRepository;

    public DoctorAvailability saveDoctorAvailability(DoctorAvailability availability) {
        return doctorAvailabilityRepository.save(availability);
    }

    public List<DoctorAvailability> getDoctorAvailabilityByDoctorId(Long doctorId) {
        return doctorAvailabilityRepository.findByDoctorId(doctorId);
    }

    public List<DoctorAvailability> getDoctorAvailabilityByDoctorIdAndDayOfWeek(Long doctorId, DayOfWeek dayOfWeek) {
        return doctorAvailabilityRepository.findByDoctorIdAndDayOfWeek(doctorId, dayOfWeek);
    }

    public Optional<DoctorAvailability> getDoctorAvailabilityById(Long id) {
        return doctorAvailabilityRepository.findById(id);
    }

    public void deleteDoctorAvailability(Long id) {
        doctorAvailabilityRepository.deleteById(id);
    }
}
