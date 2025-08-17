package com.docubot.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.docubot.exception.GoogleAccountOnlyException;

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(GoogleAccountOnlyException.class)
    public ResponseEntity<Map<String,Object>> handleGoogleOnly(GoogleAccountOnlyException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(Map.of("code","ACCOUNT_GOOGLE_ONLY","message", ex.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String,Object>> handleIllegalArgument(IllegalArgumentException ex) {
        // Treat as bad credentials in login context
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(Map.of("code","INVALID_CREDENTIALS","message",
                ex.getMessage() != null ? ex.getMessage() : "Invalid credentials"));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String,Object>> handleGeneric(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(Map.of("code","INTERNAL_ERROR","message","Something went wrong"));
    }
}
