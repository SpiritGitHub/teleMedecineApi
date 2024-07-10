package com.sp.telemedecine.services.Autre;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                ex.getMessage(),
                request.getDescription(false)
        );
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(errorResponse);
    }

    public class AvailabilityException extends RuntimeException {
        public static class PastOrFutureError extends RuntimeException {
            public PastOrFutureError(String message) {
                super(message);
            }
        }

        public static class OverlapError extends RuntimeException {
            public OverlapError(String message) {
                super(message);
            }
        }

        public static class InvalidTimeRangeError extends RuntimeException {
            public InvalidTimeRangeError(String message) {
                super(message);
            }
        }
    }
    // You can add more handlers here for other exceptions

    // Inner class for error response
    public static class ErrorResponse {
        private LocalDateTime timestamp;
        private int status;
        private String error;
        private String message;
        private String path;

        public ErrorResponse(LocalDateTime timestamp, int status, String error, String message, String path) {
            this.timestamp = timestamp;
            this.status = status;
            this.error = error;
            this.message = message;
            this.path = path;
        }

        // Getters and setters
        public LocalDateTime getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        @Override
        public String toString() {
            return "ErrorResponse{" +
                    "timestamp=" + timestamp +
                    ", status=" + status +
                    ", error='" + error + '\'' +
                    ", message='" + message + '\'' +
                    ", path='" + path + '\'' +
                    '}';
        }
    }

    // Define custom exceptions within the handler
    public static class UserAlreadyExistsException extends RuntimeException {
        public UserAlreadyExistsException(String message) {
            super(message);
        }
    }

    public static class PseudoAlreadyExistsException extends RuntimeException {
        public PseudoAlreadyExistsException(String message) {
            super(message);
        }
    }

    public static class InvalidPasswordException extends RuntimeException {
        public InvalidPasswordException(String message) {
            super(message);
        }
    }

    // Uncomment and add more exception handlers as needed
//    @ExceptionHandler(UserAlreadyExistsException.class)
//    public ResponseEntity<ErrorResponse> handleUserAlreadyExistsException(UserAlreadyExistsException ex, WebRequest request) {
//        ErrorResponse errorResponse = new ErrorResponse(LocalDateTime.now(), HttpStatus.CONFLICT.value(), "User Already Exists", ex.getMessage(), request.getDescription(false));
//        return ResponseEntity
//                .status(HttpStatus.CONFLICT)
//                .contentType(MediaType.APPLICATION_JSON)
//                .body(errorResponse);
//    }
//
//    @ExceptionHandler(PseudoAlreadyExistsException.class)
//    public ResponseEntity<ErrorResponse> handlePseudoAlreadyExistsException(PseudoAlreadyExistsException ex, WebRequest request) {
//        ErrorResponse errorResponse = new ErrorResponse(LocalDateTime.now(), HttpStatus.CONFLICT.value(), "Pseudo Already Exists", ex.getMessage(), request.getDescription(false));
//        return ResponseEntity
//                .status(HttpStatus.CONFLICT)
//                .contentType(MediaType.APPLICATION_JSON)
//                .body(errorResponse);
//    }
//
//    @ExceptionHandler(InvalidPasswordException.class)
//    public ResponseEntity<ErrorResponse> handleInvalidPasswordException(InvalidPasswordException ex, WebRequest request) {
//        ErrorResponse errorResponse = new ErrorResponse(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(), "Invalid Password", ex.getMessage(), request.getDescription(false));
//        return ResponseEntity
//                .status(HttpStatus.BAD_REQUEST)
//                .contentType(MediaType.APPLICATION_JSON)
//                .body(errorResponse);
//    }
//
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ErrorResponse> handleException(Exception ex, WebRequest request) {
//        ErrorResponse errorResponse = new ErrorResponse(LocalDateTime.now(), HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal Server Error", ex.getMessage(), request.getDescription(false));
//        return ResponseEntity
//                .status(HttpStatus.INTERNAL_SERVER_ERROR)
//                .contentType(MediaType.APPLICATION_JSON)
//                .body(errorResponse);
//    }
}
