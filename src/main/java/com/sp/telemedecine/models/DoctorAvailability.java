package com.sp.telemedecine.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "doctor_availability")
@Data
public class DoctorAvailability {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private boolean isTaken;

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    @JsonBackReference
    private Doctor doctor;
}
