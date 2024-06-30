package com.sp.Medecine.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

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
}
