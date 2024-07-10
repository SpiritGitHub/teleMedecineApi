package com.sp.telemedecine.services.Autre;

import com.sp.telemedecine.models.Prescription;
import com.sp.telemedecine.repository.PrescriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PrescriptionService {
    @Autowired
    private PrescriptionRepository prescriptionRepository;

    public List<Prescription> getAllPrescriptions() {
        return prescriptionRepository.findAll();
    }

    public Prescription getPrescriptionById(Long id) {
        return prescriptionRepository.findById(id).orElse(null);
    }

    public Prescription createPrescription(Prescription prescription) {
        return prescriptionRepository.save(prescription);
    }

    public Prescription updatePrescription(Long id, Prescription prescriptionDetails) {
        Prescription prescription = prescriptionRepository.findById(id).orElse(null);
        if (prescription != null) {
            prescription.setMedication(prescriptionDetails.getMedication());
            prescription.setDosage(prescriptionDetails.getDosage());
            prescription.setFrequency(prescriptionDetails.getFrequency());
            prescription.setStartDate(prescriptionDetails.getStartDate());
            prescription.setEndDate(prescriptionDetails.getEndDate());
            return prescriptionRepository.save(prescription);
        }
        return null;
    }

    public void deletePrescription(Long id) {
        prescriptionRepository.deleteById(id);
    }

    public List<Prescription> getPrescriptionsByDoctorId(Long doctorId) {
        return prescriptionRepository.findByAppointment_Doctor_Id(doctorId);
    }

    public List<Prescription> getPrescriptionsByPatientId(Long patientId) {
        return prescriptionRepository.findByAppointment_Patient_Id(patientId);
    }

//    public List<Prescription> getPrescriptionsByAppointmentId(Long appointmentId) {
//        return prescriptionRepository.findByAppointmentId(appointmentId);
//    }
}


