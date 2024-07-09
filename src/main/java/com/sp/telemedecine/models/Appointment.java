package com.sp.telemedecine.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "appointment")
@Data
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "patient_id", referencedColumnName = "id")
    @JsonBackReference("patient-appointment")
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "doctor_id", referencedColumnName = "id")
    @JsonBackReference("doctor-appointment")
    private Doctor doctor;

    private LocalDate appointmentDate;
    private LocalTime appointmentTime;

    private String consultationType;
    private String reason;

    @OneToOne(mappedBy = "appointment", cascade = CascadeType.ALL)
    @JsonManagedReference("appointment-doctorNote")
    private DoctorNote doctorNote;

    @OneToMany(mappedBy = "appointment", cascade = CascadeType.ALL)
    @JsonManagedReference("appointment-prescription")
    private List<Prescription> prescriptions;

    @Enumerated(EnumType.STRING)
    private AppointmentStatus status;

    @Transient
    public LocalDateTime getAppointmentDateTime() {
        return LocalDateTime.of(appointmentDate, appointmentTime);
    }
}
