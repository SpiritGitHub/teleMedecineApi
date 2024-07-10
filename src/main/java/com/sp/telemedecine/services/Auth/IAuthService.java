package com.sp.telemedecine.services.Auth;

import com.sp.telemedecine.dto.AuthResponse;
import com.sp.telemedecine.dto.SignupRequest;
import com.sp.telemedecine.dto.SinginRequest;
import com.sp.telemedecine.models.User;

import java.util.Optional;

public interface IAuthService {
    User signupPatient(SignupRequest signupRequest,  String notificationToken);
    User signupMedecin(SignupRequest signupRequest,  String notificationToken);
    AuthResponse signin(SinginRequest signinRequest);
    Optional<User> findByEmail(String email);
}
