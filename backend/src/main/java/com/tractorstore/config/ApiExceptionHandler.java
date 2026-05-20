package com.tractorstore.config;

import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class ApiExceptionHandler {

  @ExceptionHandler(IllegalArgumentException.class)
  ResponseEntity<Map<String, Object>> handleBadRequest(IllegalArgumentException ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(Map.of("code", "BAD_REQUEST", "message", ex.getMessage()));
  }

  @ExceptionHandler(ResponseStatusException.class)
  ResponseEntity<Map<String, Object>> handleStatus(ResponseStatusException ex) {
    HttpStatus status = HttpStatus.valueOf(ex.getStatusCode().value());
    String code = status.name();
    return ResponseEntity.status(status)
        .body(
            Map.of(
                "code",
                code,
                "message",
                ex.getReason() == null ? status.getReasonPhrase() : ex.getReason()));
  }

  @ExceptionHandler(Exception.class)
  ResponseEntity<Map<String, Object>> handleUnexpected(Exception ex) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(Map.of("code", "INTERNAL_ERROR", "message", "Error interno del servidor"));
  }
}
