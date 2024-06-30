package com.sp.Medecine.controller;

import com.sp.Medecine.dto.AuthResponse;
import com.sp.Medecine.dto.SignupRequest;
import com.sp.Medecine.dto.SinginRequest;
import com.sp.Medecine.models.User;
import com.sp.Medecine.services.Auth.IAuthService;
import com.sp.Medecine.services.Autre.ErrorResponse;
import com.sp.Medecine.services.Autre.GlobalExceptionHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth/")
@RequiredArgsConstructor
public class AuthController {

    private final IAuthService authService;

    @PostMapping("signup-patient")
    public ResponseEntity<?> signupPatient(@RequestBody SignupRequest signUpRequest) {
        try {
            User user = authService.signupPatient(signUpRequest);
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
    public ResponseEntity<?> signupMedecin(@RequestBody SignupRequest signupRequest) {
        try {
            User user = authService.signupMedecin(signupRequest);
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
}
