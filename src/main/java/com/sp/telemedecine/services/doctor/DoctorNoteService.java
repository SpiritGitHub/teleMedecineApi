package com.sp.telemedecine.services.doctor;

import com.sp.telemedecine.models.DoctorNote;
import com.sp.telemedecine.repository.DoctorNoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorNoteService {
    @Autowired
    private DoctorNoteRepository doctorNoteRepository;

    public List<DoctorNote> getAllDoctorNotes() {
        return doctorNoteRepository.findAll();
    }

    public DoctorNote getDoctorNoteById(Long id) {
        return doctorNoteRepository.findById(id).orElse(null);
    }

    public DoctorNote createDoctorNote(DoctorNote doctorNote) {
        return doctorNoteRepository.save(doctorNote);
    }

    public DoctorNote updateDoctorNote(Long id, DoctorNote doctorNoteDetails) {
        DoctorNote doctorNote = doctorNoteRepository.findById(id).orElse(null);
        if (doctorNote != null) {
            doctorNote.setNoteContent(doctorNoteDetails.getNoteContent());
            return doctorNoteRepository.save(doctorNote);
        }
        return null;
    }

    public void deleteDoctorNote(Long id) {
        doctorNoteRepository.deleteById(id);
    }

    public List<DoctorNote> getDoctorNotesByDoctorId(Long doctorId) {
        return doctorNoteRepository.findByAppointment_Doctor_Id(doctorId);
    }
    public List<DoctorNote> getDoctorNotesByAppointmentId(Long appointmentId) {
        return doctorNoteRepository.findByAppointmentId(appointmentId);
    }
}

