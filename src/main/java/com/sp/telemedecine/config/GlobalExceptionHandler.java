package com.sp.telemedecine.config;//package com.sp.Medecine.config;
//
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//
//@RestControllerAdvice
//public class GlobalExceptionHandler {
//
////    @ExceptionHandler(Exception.class)
////    public ResponseEntity<ErrorResponse> handleException(Exception ex) {
////        ErrorResponse errorResponse = new ErrorResponse("An error occurred", ex.getMessage());
////        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
////                .contentType(MediaType.APPLICATION_JSON) // Ensure JSON response
////                .body(errorResponse);
////    }
//}
//
//class ErrorResponse {
//    private String message;
//    private String detail;
//
//    public ErrorResponse(String message, String detail) {
//        this.message = message;
//        this.detail = detail;
//    }
//
//    // Getters and Setters
//}
//
