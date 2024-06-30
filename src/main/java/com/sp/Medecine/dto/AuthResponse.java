package com.sp.Medecine.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class AuthResponse {

    private String jwt;
    private String refreshToken;
    private long expiresAt;
    private String userId;
    private String userName;
    private String email;
    private String pseudo;
    private String role;
    private String message;
}
