package com.sp.telemedecine.controller;

import com.sp.telemedecine.dto.*;
import com.sp.telemedecine.models.User;
import com.sp.telemedecine.services.Auth.AuthServiceImpl;
import com.sp.telemedecine.services.Auth.IAuthService;
import com.sp.telemedecine.services.Autre.ErrorResponse;
import com.sp.telemedecine.services.Autre.GlobalExceptionHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth/")
@RequiredArgsConstructor
public class AuthController {

    private final IAuthService authService;

    @Autowired
    private AuthServiceImpl authServiceImpl;

    @PostMapping("signup-patient")
    public ResponseEntity<?> signupPatient(@RequestBody SignupRequest signUpRequest, @RequestParam String notificationToken) {
        try {
            User user = authService.signupPatient(signUpRequest, notificationToken);
            return ResponseEntity.ok(user);
        } catch (GlobalExceptionHandler.UserAlreadyExistsException |
                 GlobalExceptionHandler.PseudoAlreadyExistsException |
                 GlobalExceptionHandler.InvalidPasswordException e) {
            return handleSignupException(e);
        } catch (Exception e) {
            return handleGenericException(e);
        }
    }

    @PostMapping("signup-doctor")
    public ResponseEntity<?> signupMedecin(@RequestBody SignupRequest signupRequest, @RequestParam String notificationToken) {
        try {
            User user = authService.signupMedecin(signupRequest, notificationToken);
            return ResponseEntity.ok(user);
        } catch (GlobalExceptionHandler.UserAlreadyExistsException |
                 GlobalExceptionHandler.PseudoAlreadyExistsException |
                 GlobalExceptionHandler.InvalidPasswordException e) {
            return handleSignupException(e);
        } catch (Exception e) {
            return handleGenericException(e);
        }
    }

    @PostMapping("login")
    public ResponseEntity<AuthResponse> signin(@RequestBody SinginRequest singinRequest) {
        return ResponseEntity.ok(authService.signin(singinRequest));
    }

    private ResponseEntity<ErrorResponse> handleSignupException(RuntimeException e) {
        HttpStatus status = HttpStatus.CONFLICT;
        String error = e instanceof GlobalExceptionHandler.UserAlreadyExistsException ? "Email Already Exists" :
                e instanceof GlobalExceptionHandler.PseudoAlreadyExistsException ? "Pseudo Already Exists" :
                        "Invalid Password";
        ErrorResponse errorResponse = new ErrorResponse(status, error, e.getMessage());
        return new ResponseEntity<>(errorResponse, status);
    }

    private ResponseEntity<ErrorResponse> handleGenericException(Exception e) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", "An error occurred during signup");
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping("/confirm")
    public ResponseEntity<Boolean> confirmUser(@RequestBody ConfirmationRequest request) {
        System.out.println("Received userId: " + request.getUserId() + ", confirmationCode: " + request.getConfirmationCode());
        return ResponseEntity.ok(authServiceImpl.confirmUser(request.getUserId(), request.getConfirmationCode()));
    }

    @PostMapping("/password-change/initiate")
    public void initiatePasswordChange(@RequestParam String email) {
        authServiceImpl.initiatePasswordChange(email);
    }

    @PostMapping("/password-change/confirm")
    public boolean confirmPasswordChange(@RequestBody PasswordChangeRequest passwordChangeRequest) {
        return authServiceImpl.confirmPasswordChange(passwordChangeRequest);
    }
}
