package com.sp.Medecine.services.Auth;

import com.sp.Medecine.dto.AuthResponse;
import com.sp.Medecine.dto.RefreshTokenRequest;
import com.sp.Medecine.dto.SignupRequest;
import com.sp.Medecine.dto.SinginRequest;
import com.sp.Medecine.models.Doctor;
import com.sp.Medecine.models.Patient;
import com.sp.Medecine.models.User;
import com.sp.Medecine.models.UserRole;
import com.sp.Medecine.repository.DoctorRepo;
import com.sp.Medecine.repository.PatientRepo;
import com.sp.Medecine.repository.UserRepo;
import com.sp.Medecine.services.Autre.GlobalExceptionHandler;
import com.sp.Medecine.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements IAuthService {

    private final UserRepo userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final PatientRepo patientProfileRepository;
    private final DoctorRepo medecinProfileRepository;
    private final AuthenticationManager authenticationManager;

    @Override
    public User signupPatient(SignupRequest signupRequest) {
        validateSignupRequest(signupRequest);
        User user = createUser(signupRequest, UserRole.PATIENT);
        Patient patientProfile = new Patient();
        patientProfile.setUser(user);
        patientProfileRepository.save(patientProfile);
        return user;
    }

    @Override
    public User signupMedecin(SignupRequest signupRequest) {
        validateSignupRequest(signupRequest);
        User user = createUser(signupRequest, UserRole.DOCTOR);
        Doctor medecinProfile = new Doctor();
        medecinProfile.setUser(user);
        medecinProfileRepository.save(medecinProfile);
        return user;
    }

    private void validateSignupRequest(SignupRequest signupRequest) {
        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            throw new GlobalExceptionHandler.UserAlreadyExistsException("Email already exists");
        }
        if (userRepository.existsByPseudo(signupRequest.getPseudo())) {
            throw new GlobalExceptionHandler.PseudoAlreadyExistsException("Pseudo already exists");
        }
        if (signupRequest.getPassword().length() < 6) {
            throw new GlobalExceptionHandler.InvalidPasswordException("Password is too short");
        }
    }

    private User createUser(SignupRequest signupRequest, UserRole role) {
        User user = new User();
        user.setEmail(signupRequest.getEmail());
        user.setUserRole(role);
        user.setPseudo(signupRequest.getPseudo());
        user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public AuthResponse signin(SinginRequest signinRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        signinRequest.getEmail(),
                        signinRequest.getPassword()
                )
        );

        User user = userRepository.findByEmail(signinRequest.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

        String jwt = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(new HashMap<>(), user);
        long expiresAt = System.currentTimeMillis() + 1000 * 60 * 60 * 10; // 10 hours
        String role = user.getAuthorities().iterator().next().getAuthority();

        return buildAuthResponse(user, jwt, refreshToken, expiresAt, role);
    }

    private AuthResponse buildAuthResponse(User user, String jwt, String refreshToken, long expiresAt, String role) {
        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwt(jwt);
        authResponse.setRefreshToken(refreshToken);
        authResponse.setExpiresAt(expiresAt);
        authResponse.setUserId(user.getUserId().toString());
        authResponse.setEmail(user.getEmail());
        authResponse.setPseudo(user.getPseudo());
        authResponse.setRole(role);
        authResponse.setMessage("Authentication successful");
        return authResponse;
    }

    public AuthResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        String userEmail = jwtService.extractUserName(refreshTokenRequest.getRefreshtoken());
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("Invalid refresh token"));

        if (jwtService.isTokenValid(refreshTokenRequest.getRefreshtoken(), user)) {
            String jwt = jwtService.generateToken(user);
            return buildAuthResponse(user, jwt, refreshTokenRequest.getRefreshtoken(),
                    System.currentTimeMillis() + 1000 * 60 * 60 * 10, // 10 hours
                    user.getUserRole().toString());
        }

        throw new IllegalArgumentException("Invalid refresh token");
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
