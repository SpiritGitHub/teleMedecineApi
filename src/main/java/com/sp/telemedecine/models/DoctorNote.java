package com.sp.telemedecine.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "DoctorNote")
public class DoctorNote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long noteId;

    private String noteContent;

    @ManyToOne
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @OneToOne
    @JoinColumn(name = "appointment_id", nullable = false)
    @JsonBackReference("appointment-doctorNote")
    private Appointment appointment;
}
