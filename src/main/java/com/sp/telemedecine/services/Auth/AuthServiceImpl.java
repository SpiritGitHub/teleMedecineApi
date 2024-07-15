package com.sp.telemedecine.services.Auth;

import com.sp.telemedecine.dto.*;
import com.sp.telemedecine.models.Doctor;
import com.sp.telemedecine.models.Patient;
import com.sp.telemedecine.models.User;
import com.sp.telemedecine.models.UserRole;
import com.sp.telemedecine.services.emailService.ConfirmationCodeRepo;
import com.sp.telemedecine.repository.DoctorRepo;
import com.sp.telemedecine.repository.PatientRepo;
import com.sp.telemedecine.repository.UserRepo;
import com.sp.telemedecine.services.Autre.GlobalExceptionHandler;
import com.sp.telemedecine.services.emailService.ConfirmationCodeGen;
import com.sp.telemedecine.services.emailService.EmailService;
import com.sp.telemedecine.services.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements IAuthService {

    @Autowired
    private ConfirmationCodeGen confirmationCodeGen;
    @Autowired
    private EmailService emailService;
    @Autowired
    private ConfirmationCodeRepo confirmationCodeRepo;

    private final UserRepo userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final PatientRepo patientProfileRepository;
    private final DoctorRepo medecinProfileRepository;
    private final AuthenticationManager authenticationManager;

    public User signupPatient(SignupRequest signupRequest) {
        validateSignupRequest(signupRequest);
        User user = createUser(signupRequest, UserRole.PATIENT);
        String confirmationCode = confirmationCodeGen.generateConfirmationCode();

        confirmationCodeRepo.storeConfirmationCode(user.getUserId(), confirmationCode);
        emailService.sendConfirmationEmail(user.getEmail(), confirmationCode);

        user.setActive(false);
        userRepository.save(user);

        Patient patientProfile = new Patient();
        patientProfile.setUser(user);
        patientProfileRepository.save(patientProfile);

        return user;
    }

    public User signupMedecin(SignupRequest signupRequest) {
        validateSignupRequest(signupRequest);
        User user = createUser(signupRequest, UserRole.DOCTOR);
        String confirmationCode = confirmationCodeGen.generateConfirmationCode();

        confirmationCodeRepo.storeConfirmationCode(user.getUserId(), confirmationCode);
        emailService.sendConfirmationEmail(user.getEmail(), confirmationCode);

        user.setActive(false);
        userRepository.save(user);

        Doctor medecinProfile = new Doctor();
        medecinProfile.setUser(user);
        medecinProfileRepository.save(medecinProfile);

        return user;
    }

    public boolean confirmUser(Long userId, String confirmationCode) {
        String storedCode = confirmationCodeRepo.retrieveConfirmationCode(userId);
        if (storedCode != null && storedCode.equals(confirmationCode)) {
            Optional<User> userOptional = userRepository.findById(userId);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                user.setActive(true);
                userRepository.save(user);
                confirmationCodeRepo.removeConfirmationCode(userId);
                return true;
            }
        }
        return false;
    }


    public void initiatePasswordChange(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            String confirmationCode = confirmationCodeGen.generateConfirmationCode();
            confirmationCodeRepo.storeConfirmationCode(user.getUserId(), confirmationCode);
            emailService.sendConfirmationEmail(user.getEmail(), confirmationCode);
        }
    }

    public boolean confirmPasswordChange(PasswordChangeRequest passwordChangeRequest) {
        Long userId = Long.parseLong(passwordChangeRequest.getUserId());
        if (confirmUser(userId, passwordChangeRequest.getConfirmationCode())) {
            Optional<User> userOptional = userRepository.findById(userId);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                user.setPassword(passwordEncoder.encode(passwordChangeRequest.getNewPassword()));
                userRepository.save(user);
                return true;
            }
        }
        return false;
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
