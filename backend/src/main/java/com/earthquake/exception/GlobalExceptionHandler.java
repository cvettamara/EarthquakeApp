package com.earthquake.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

// global errror handler - from all controllers
@RestControllerAdvice
public class GlobalExceptionHandler {

    // catch errors connected to USGS API
    @ExceptionHandler(ApiUnavailableException.class)
    public ResponseEntity<Map<String, Object>> handleApiError(ApiUnavailableException ex) {

        Map<String, Object> error = new HashMap<>();
        error.put("timestamp", LocalDateTime.now());
        error.put("error", "USGS API Error");
        error.put("message", ex.getMessage());

        // 503 Service Unavailable
        return new ResponseEntity<>(error, HttpStatus.SERVICE_UNAVAILABLE);
    }

    // other errors (RuntimeException)
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntime(RuntimeException ex) {

        Map<String, Object> error = new HashMap<>();
        error.put("timestamp", LocalDateTime.now());
        error.put("error", "Server Error");
        error.put("message", ex.getMessage());

        //500 Internal Server Error
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}