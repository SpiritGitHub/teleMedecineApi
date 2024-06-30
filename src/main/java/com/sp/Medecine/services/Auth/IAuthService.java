package com.sp.Medecine.services.Auth;

import com.sp.Medecine.dto.AuthResponse;
import com.sp.Medecine.dto.SignupRequest;
import com.sp.Medecine.dto.SinginRequest;
import com.sp.Medecine.models.User;

import java.util.Optional;

public interface IAuthService {
    User signupPatient(SignupRequest signupRequest);
    User signupMedecin(SignupRequest signupRequest);
    AuthResponse signin(SinginRequest signinRequest);
    Optional<User> findByEmail(String email);
}
