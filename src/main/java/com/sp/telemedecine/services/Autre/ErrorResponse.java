package com.sp.telemedecine.services.Autre;

import org.springframework.http.HttpStatus;

public class ErrorResponse {
    private HttpStatus status;
    private String error;
    private String message;

    public ErrorResponse(HttpStatus status, String error, String message) {
        this.status = status;
        this.error = error;
        this.message = message;
    }

    // Getters and setters
}

