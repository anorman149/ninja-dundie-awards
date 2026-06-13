package com.ninjaone.dundie_awards.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({Exception.class, RuntimeException.class})
    public ResponseEntity<?> handleGenericException(Exception ex, ServletWebRequest request) {
        return standardizedError(ex, HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler({NotFoundException.class})
    public ResponseEntity<?> handleNotFoundException(NotFoundException ex, ServletWebRequest request) {
        return standardizedError(ex, HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler({ResponseStatusException.class})
    public ResponseEntity<?> handleResponseStatusException(ResponseStatusException ex, ServletWebRequest request) {
        return standardizedError(ex, HttpStatus.valueOf(ex.getStatusCode().value()), request);
    }

    private ResponseEntity<?> standardizedError(Exception ex, HttpStatus status, ServletWebRequest request) {
        String path = request.getRequest().getServletPath();
        log.error(ex.getMessage(), ex);
        return ResponseEntity.status(status).body(new ErrorResponse(ex.getMessage(), path));
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class ErrorResponse {
        private String message;
        private String path;
    }
}
