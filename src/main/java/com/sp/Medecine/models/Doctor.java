package com.sp.Medecine.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "userId")
    private User user;

    private String specialization;
    private String gender;
    private String address;
    private String contactNumber;

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<DoctorAvailability> availabilities;

}
