package com.example.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.DTO.RestAPIResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // ✅ Duplicate Vendor
    @ExceptionHandler(DuplicateVendorException.class)
    public ResponseEntity<RestAPIResponse> handleDuplicateVendor(DuplicateVendorException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new RestAPIResponse("error", ex.getMessage(), null));
    }

    // ✅ Illegal State
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<RestAPIResponse> handleIllegalState(IllegalStateException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new RestAPIResponse("error", ex.getMessage(), null));
    }

    // ✅ Resource Not Found
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<RestAPIResponse> handleNotFound(ResourceNotFoundException ex) {
        return new ResponseEntity<>(
                new RestAPIResponse("FAIL", ex.getMessage(), null),
                HttpStatus.NOT_FOUND
        );
    }

    // ✅ Runtime Exception
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<RestAPIResponse> handleRuntime(RuntimeException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new RestAPIResponse("fail", ex.getMessage(), null));
    }

    // ✅ ONLY ONE General Exception (KEEP THIS)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<RestAPIResponse> handleGeneral(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new RestAPIResponse("error", "Something went wrong!", null));
    }
}