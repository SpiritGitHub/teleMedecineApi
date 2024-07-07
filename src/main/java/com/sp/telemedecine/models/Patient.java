package com.sp.telemedecine.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "patient")
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "userId")
    private User user;

    private String lastName;
    private String firstName;
    private String gender;
    private Date birthday;
    private String address;
    private String contactNumber;
    private String profilePictureUrl;

    @OneToMany(mappedBy = "patient")
    @JsonManagedReference
    private List<Appointment> appointments;

//    @OneToMany(mappedBy = "patient")
//    private List<Prescription> prescriptions;
//
//    @OneToMany(mappedBy = "patient")
//    private List<TreatmentPlan> treatmentPlans;

    @OneToMany(mappedBy = "patient")
    private List<MedicalHistory> medicalHistories;
}