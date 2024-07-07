package com.sp.telemedecine.dto;

import com.sp.telemedecine.models.DoctorNote;
import com.sp.telemedecine.models.Prescription;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Data
@Setter
@Getter
public class CompleteAppointmentRequest {

    private Long appointmentId;
    private DoctorNote doctorNote;
    private List<Prescription> prescriptions;
}

