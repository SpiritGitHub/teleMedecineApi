package com.sp.telemedecine.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "Doctor")
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<DoctorAvailability> availabilities;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "userId")
    private User user;

    private String specialization;
    private String gender;
    private String address;
    private String contactNumber;

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL)
    @JsonManagedReference("doctor-appointment")
    private List<Appointment> appointments;

    @OneToMany(mappedBy = "doctor")
    private List<Prescription> prescriptions;

    @OneToMany(mappedBy = "doctor")
    private List<DoctorNote> doctorNotes;
}
