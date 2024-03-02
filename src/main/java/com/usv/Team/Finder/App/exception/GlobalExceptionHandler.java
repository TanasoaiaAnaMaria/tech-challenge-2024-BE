package com.usv.Team.Finder.App.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RegistrationException.class)
    public ResponseEntity<Map<String, Object>> handleRegistrationException(RegistrationException ex) {
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("message", ex.getMessage());
        responseBody.put("status", ex.getStatus().value());
        responseBody.put("error", true);

        return new ResponseEntity<>(responseBody, ex.getStatus());
    }

}