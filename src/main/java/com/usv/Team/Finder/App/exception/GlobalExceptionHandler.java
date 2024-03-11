package com.usv.Team.Finder.App.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RegistrationException.class)
    public ResponseEntity<Map<String, Object>> handleRegistrationException(RegistrationException ex) {
        return buildResponseEntity(ex.getMessage(), ex.getStatus(), true);
    }

    @ExceptionHandler(FunctionalException.class)
    public ResponseEntity<Map<String, Object>> handleFunctionalException(FunctionalException ex) {
        return buildResponseEntity(ex.getMessage(), ex.getStatus(), true);
    }

    private ResponseEntity<Map<String, Object>> buildResponseEntity(String message, HttpStatus status, boolean error) {
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("message", message);
        responseBody.put("status", status.value());
        responseBody.put("error", error);

        return new ResponseEntity<>(responseBody, status);
    }
}
