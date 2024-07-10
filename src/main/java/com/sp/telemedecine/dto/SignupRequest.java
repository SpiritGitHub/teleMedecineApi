package com.sp.telemedecine.dto;

import lombok.Data;

@Data
public class SignupRequest {

    private String password;
    private String email;
    private String pseudo;
}
