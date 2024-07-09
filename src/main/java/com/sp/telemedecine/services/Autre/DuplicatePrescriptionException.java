package com.sp.telemedecine.services.Autre;

public class DuplicatePrescriptionException extends RuntimeException {
    public DuplicatePrescriptionException(String message) {
        super(message);
    }
}
