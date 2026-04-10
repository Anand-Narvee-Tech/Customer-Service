package com.example.exception;

import org.springframework.dao.DataAccessException;
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
    
    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<RestAPIResponse> handleDatabaseException(DataAccessException ex) {

        String message = getCleanDatabaseMessage(ex);

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new RestAPIResponse("fail", message, null));
    }

 // 🔥 NEW: Clean DB message extractor
    private String getCleanDatabaseMessage(Exception ex) {

        String msg = ex.getMessage();

        if (msg == null) return "Database error occurred";

        // Table not found
        if (msg.contains("relation") && msg.contains("does not exist")) {
            return "Requested data table not found. Please contact admin.";
        }

        // Duplicate key
        if (msg.contains("duplicate key")) {
            return "Duplicate record already exists.";
        }

        // Foreign key issue
        if (msg.contains("violates foreign key constraint")) {
            return "Invalid reference data provided.";
        }

        // Null value issue
        if (msg.contains("null value")) {
            return "Required field is missing.";
        }

        return "Database error occurred";
    }
}