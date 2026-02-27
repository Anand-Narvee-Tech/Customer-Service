package com.example.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.DTO.RestAPIResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DuplicateVendorException.class)
    public ResponseEntity<RestAPIResponse> handleDuplicateVendor(DuplicateVendorException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new RestAPIResponse("error", ex.getMessage(), null));
    }

    //  ADD THIS
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<RestAPIResponse> handleIllegalState(IllegalStateException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT) // 409
                .body(new RestAPIResponse("error", ex.getMessage(), null));
    }

    //  Keep this LAST
    @ExceptionHandler(Exception.class)
    public ResponseEntity<RestAPIResponse> handleGeneral(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new RestAPIResponse("error", "Something went wrong!", null));
    }
    
    
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntime(RuntimeException ex) {

        Map<String, Object> error = new HashMap<>();
        error.put("status", "error");
        error.put("message", ex.getMessage());
        error.put("data", null);

        return ResponseEntity.badRequest().body(error);
    }
}
