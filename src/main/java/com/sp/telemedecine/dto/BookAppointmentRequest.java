package com.sp.telemedecine.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Setter
@Getter
public class BookAppointmentRequest {
    private Long patientId;
    private Long doctorId;
    private LocalDate appointmentDate;
    private LocalTime appointmentTime;
    private String reason;
}

